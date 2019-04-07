package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverState: AsyncResult<DiscoverState>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
}

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) : BaseUseCase() {
    val state = PublishSubject.create<AsyncResult<DiscoverState>>()
    private val loadIntent = PublishSubject.create<DiscoverIntent.Load>()
    private val selectGenreIntent = PublishSubject.create<DiscoverIntent.SelectGenre>()
    private val clearSelectedGenreIntent = PublishSubject.create<DiscoverIntent.ClearSelectedGenre>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)

        val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)
        val allIntents = Observable.merge(
                loadIntent.flatMap(this::load),
                selectGenreIntent.flatMap(this::selectGenre),
                clearSelectedGenreIntent.flatMap(this::clearSelectedGenre))

        allIntents.scan(initialState, this::reduce)
                .distinctUntilChanged()
                .safelySubscribe(this, state::onNext)

        perform(DiscoverIntent.Load)
    }

    fun perform(intent: DiscoverIntent) = when (intent) {
        is DiscoverIntent.Load -> loadIntent.onNext(intent)
        is DiscoverIntent.SelectGenre -> selectGenreIntent.onNext(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenreIntent.onNext(intent)
    }

    private fun load(intent: DiscoverIntent.Load): Observable<DiscoverStateChange> {
        return browse()
                .map(DiscoverStateChange::DiscoverResultsAvailable)
                .compose(schedulerContext.applySchedulers())
    }

    private fun selectGenre(intent: DiscoverIntent.SelectGenre): Observable<DiscoverStateChange> {
        val selectedChange: DiscoverStateChange = DiscoverStateChange.GenreSelected(intent.genre)
        return movies.discoverByGenre(intent.genre.id).map(this::buildGenreResults)
                .startWith(selectedChange)
                .compose(schedulerContext.applySchedulers())
    }

    private fun clearSelectedGenre(intent: DiscoverIntent.ClearSelectedGenre): Observable<DiscoverStateChange> {
        return Observable.just(DiscoverStateChange.SelectedGenreCleared)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }

    private fun reduce(previousState: AsyncResult<DiscoverState>, change: DiscoverStateChange): AsyncResult<DiscoverState> {
        return when (change) {
            is DiscoverStateChange.DiscoverResultsAvailable -> change.discoverState
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

    private fun browse(): DataStream<DiscoverState> {
        return Observables.zip(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres()
        ) { nowPlaying, popular, genres ->
            AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                    .handleNetworkErrors()
        }
    }
}