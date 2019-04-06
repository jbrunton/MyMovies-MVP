package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.*
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.PublishSubject

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) : BaseUseCase() {
    val state = PublishSubject.create<AsyncResult<DiscoverState>>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)
        load()
    }

    fun retry() {
        load()
    }

    fun showGenre(genre: Genre) {
        val previousState: Observable<AsyncResult<DiscoverState>> = state.lastOrError().toObservable()
        val genreResults: DataStream<List<Movie>> = movies.discoverByGenre(genre.id)
        val newState: Observable<AsyncResult<DiscoverState>> = Observable.combineLatest(previousState, genreResults) { previousState, genreResults ->

        }

        movies.discoverByGenre(genre.id).zipWith(state.lastOrError()) { genreResults, previousState ->

        }
    }

    private fun load() {
        Observables.zip(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres(),
                this::combineResults
        ).safelySubscribe(this, state::onNext)
    }

    private fun combineResults(
            nowPlaying: AsyncResult<List<Movie>>,
            popular: AsyncResult<List<Movie>>,
            genres: AsyncResult<List<Genre>>
    ): AsyncResult<DiscoverState> {
        return AsyncResult.zip(nowPlaying, popular, genres, AsyncResult.loading(null), ::DiscoverState)
                .handleNetworkErrors()
    }

    private fun reduceGenres(
            previousState: AsyncResult<DiscoverState>,
            genreResults: AsyncResult<List<Movie>>
    ): AsyncResult<DiscoverState> {
        return genreResults.flatMap { genreResults ->
            previousState.map { discoverState ->
                discoverState.copy(genreResults = genreResults)
            }
        }
    }

}