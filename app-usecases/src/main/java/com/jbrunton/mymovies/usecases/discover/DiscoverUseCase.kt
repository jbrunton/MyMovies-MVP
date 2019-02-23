package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
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

    private fun load() {
        Observables.zip(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres()
        ) { nowPlaying, popular, genres ->
            AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                    .handleNetworkErrors()
        }.safelySubscribe(this, state::onNext)
    }
}