package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.rxkotlin.Observables

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) {
    fun reduce(): DataStream<DiscoverState> {
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