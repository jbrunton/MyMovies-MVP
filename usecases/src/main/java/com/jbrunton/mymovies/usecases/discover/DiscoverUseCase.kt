package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import io.reactivex.rxkotlin.Observables

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) {
    fun discover(): DataStream<DiscoverState> = Observables.zip(
            movies.nowPlaying(),
            movies.popular(),
            genres.genres()
    ) { nowPlaying, popular, genres ->
        AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                .handleNetworkErrors()
    }

    fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return movies.discoverByGenre(genreId).map {
            it.handleNetworkErrors()
        }
    }
}