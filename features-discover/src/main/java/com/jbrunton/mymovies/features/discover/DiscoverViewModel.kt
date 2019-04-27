package com.jbrunton.mymovies.features.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.safelySubscribe
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.usecases.discover.DiscoverResult
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverResult>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
}

class DiscoverViewModel(container: Container) : BaseLoadingViewModel<DiscoverViewState>(container) {
    val useCase: DiscoverUseCase by inject()

    val state = PublishSubject.create<AsyncResult<DiscoverResult>>()
    private val loadIntent = BehaviorSubject.create<DiscoverIntent.Load>()
    private val selectGenreIntent = BehaviorSubject.create<DiscoverIntent.SelectGenre>()
    private val clearSelectedGenreIntent = BehaviorSubject.create<DiscoverIntent.ClearSelectedGenre>()

    override fun start() {
        super.start()
        subscribe(state) {
            viewState.postValue(DiscoverViewStateFactory.viewState(it))
        }

        val allIntents = Observable.merge(
                loadIntent.flatMap(this::load),
                selectGenreIntent.flatMap(this::selectGenre),
                clearSelectedGenreIntent.flatMap(this::clearSelectedGenre))

        val initialState: AsyncResult<DiscoverResult> = AsyncResult.loading(null)
        allIntents.scan(initialState, this::reduce)
                .distinctUntilChanged()
                .safelySubscribe(this, state::onNext)

        perform(DiscoverIntent.Load)
    }

    fun onRetryClicked() {
        perform(DiscoverIntent.Load)
    }

    fun onGenreClicked(genre: Genre) {
        perform(DiscoverIntent.SelectGenre(genre))
    }

    fun onClearGenreSelection() {
        perform(DiscoverIntent.ClearSelectedGenre)
    }

    fun onMovieSelected(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }

    private fun perform(intent: DiscoverIntent) = when (intent) {
        is DiscoverIntent.Load -> loadIntent.onNext(intent)
        is DiscoverIntent.SelectGenre -> selectGenreIntent.onNext(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenreIntent.onNext(intent)
    }

    private fun load(intent: DiscoverIntent.Load): Observable<DiscoverStateChange> {
        return useCase.discover()
                .map(DiscoverStateChange::DiscoverResultsAvailable)
                .compose(schedulerContext.applySchedulers())
    }

    private fun selectGenre(intent: DiscoverIntent.SelectGenre): Observable<DiscoverStateChange> {
        val selectedChange: DiscoverStateChange = DiscoverStateChange.GenreSelected(intent.genre)
        return useCase.discoverByGenre(intent.genre.id).map(this::buildGenreResults)
                .startWith(selectedChange)
                .compose(schedulerContext.applySchedulers())
    }

    private fun clearSelectedGenre(intent: DiscoverIntent.ClearSelectedGenre): Observable<DiscoverStateChange> {
        return Observable.just(DiscoverStateChange.SelectedGenreCleared)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }

    private fun reduce(previousState: AsyncResult<DiscoverResult>, change: DiscoverStateChange): AsyncResult<DiscoverResult> {
        return when (change) {
            is DiscoverStateChange.DiscoverResultsAvailable -> change.discoverResult
            is DiscoverStateChange.GenreSelected -> previousState.map {
                it.copy(selectedGenre = change.selectedGenre)
            }
            is DiscoverStateChange.GenreResultsAvailable -> previousState.map {
                it.copy(genreResults = change.genreResults.getOr(emptyList()))
            }
            is DiscoverStateChange.SelectedGenreCleared -> previousState.map {
                it.copy(genreResults = emptyList(), selectedGenre = null)
            }
        }
    }
}
