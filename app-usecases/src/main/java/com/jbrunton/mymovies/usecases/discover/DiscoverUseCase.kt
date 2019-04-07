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

sealed class DiscoverPartialStateChange {
    data class DiscoverStateChange(val discoverState: AsyncResult<DiscoverState>) : DiscoverPartialStateChange()
    data class GenreSelectedStateChange(val selectedGenre: Genre) : DiscoverPartialStateChange()
    data class GenreResultsStateChange(val genreResults: AsyncResult<List<Movie>>) : DiscoverPartialStateChange()
}

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) : BaseUseCase() {
    val state = PublishSubject.create<AsyncResult<DiscoverState>>()
    private val loadIntent = PublishSubject.create<Unit>()
    private val genresIntent = PublishSubject.create<Genre>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)

        val load: Observable<DiscoverPartialStateChange> = loadIntent.flatMap {
            load().map(DiscoverPartialStateChange::DiscoverStateChange)
                    .compose(schedulerContext.applySchedulers())
        }

        val searchGenre: Observable<DiscoverPartialStateChange> = genresIntent.flatMap {
            val selectedChange: DiscoverPartialStateChange = DiscoverPartialStateChange.GenreSelectedStateChange(it)
            movies.discoverByGenre(it.id).map(this::buildGenreResults)
                    .compose(schedulerContext.applySchedulers())
                    .startWith(selectedChange)
        }

        val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)
        Observable.merge(load, searchGenre)
                .scan(initialState, this::reduce)
                .distinctUntilChanged()
                .safelySubscribe(this, state::onNext)

        loadIntent.onNext(Unit)
    }

    fun retry() {
        loadIntent.onNext(Unit)
    }

    fun showGenre(genre: Genre) {
        genresIntent.onNext(genre)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverPartialStateChange {
        return DiscoverPartialStateChange.GenreResultsStateChange(genreResults)
    }

    private fun reduce(previousState: AsyncResult<DiscoverState>, change: DiscoverPartialStateChange): AsyncResult<DiscoverState> {
        return when (change) {
            is DiscoverPartialStateChange.DiscoverStateChange -> change.discoverState
            is DiscoverPartialStateChange.GenreSelectedStateChange -> previousState.map { state ->
                state.copy(selectedGenre = change.selectedGenre)
            }
            is DiscoverPartialStateChange.GenreResultsStateChange -> previousState.map { state ->
                state.copy(genreResults = change.genreResults.getOr(emptyList()))
            }
        }
    }

    private fun load(): DataStream<DiscoverState> {
        return Observables.zip(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres(),
                this::combineResults
        )
    }

    private fun combineResults(
            nowPlaying: AsyncResult<List<Movie>>,
            popular: AsyncResult<List<Movie>>,
            genres: AsyncResult<List<Genre>>
    ): AsyncResult<DiscoverState> {
        return AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                .handleNetworkErrors()
    }
}