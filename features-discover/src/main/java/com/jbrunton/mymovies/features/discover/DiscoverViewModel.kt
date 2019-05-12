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
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverResult
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    data class SelectMovie(val movie: Movie) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}

interface DiscoverListener {
    fun perform(intent: DiscoverIntent)
}

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverResult>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
    data class SelectMovie(val movie: Movie) : DiscoverStateChange()
}

class DiscoverViewModel(container: Container) : BaseLoadingViewModel<DiscoverViewState>(container), DiscoverListener {
    val scrollToGenreResults = SingleLiveEvent<Unit>()

    private val useCase: DiscoverUseCase by inject()
    private val state = PublishSubject.create<AsyncResult<DiscoverResult>>()
    private val intents = PublishSubject.create<DiscoverIntent>()

    override fun start() {
        super.start()
        subscribe(state) {
            viewState.postValue(DiscoverViewStateFactory.viewState(it))
        }
        
        val initialState: AsyncResult<DiscoverResult> = AsyncResult.loading(null)
        intents.switchMap(this::performIntent).scan(initialState, this::reduce)
                .distinctUntilChanged()
                .safelySubscribe(this, state::onNext)

        perform(DiscoverIntent.Load)
    }

    fun onRetryClicked() {
        perform(DiscoverIntent.Load)
    }

    override fun perform(intent: DiscoverIntent) {
        intents.onNext(intent)
    }

    private fun performIntent(intent: DiscoverIntent) = when (intent) {
        is DiscoverIntent.Load -> load(intent)
        is DiscoverIntent.SelectGenre -> selectGenre(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenre(intent)
        is DiscoverIntent.SelectMovie -> selectMovie(intent)
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

    private fun selectMovie(intent: DiscoverIntent.SelectMovie): Observable<DiscoverStateChange> {
        return Observable.just(DiscoverStateChange.SelectMovie(intent.movie))
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
                val genreResults = change.genreResults.getOr(emptyList())
                if (genreResults.any()) {
                    scrollToGenreResults.call()
                }
                it.copy(genreResults = genreResults)
            }
            is DiscoverStateChange.SelectedGenreCleared -> previousState.map {
                it.copy(genreResults = emptyList(), selectedGenre = null)
            }
            is DiscoverStateChange.SelectMovie -> {
                navigator.navigate(MovieDetailsRequest(change.movie.id))
                previousState
            }
        }
    }
}
