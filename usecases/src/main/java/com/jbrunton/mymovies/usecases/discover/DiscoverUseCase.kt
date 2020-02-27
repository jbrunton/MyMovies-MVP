package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import io.reactivex.rxkotlin.Observables
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.zip

class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) {
    suspend fun discover(): FlowDataStream<DiscoverResult> {
        return combine(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres())
        { nowPlaying, popular, genres ->
            AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverResult)
                    .handleNetworkErrors()

        }
    }

    fun discoverByGenre(genreId: String): DataStream<List<Movie>> {
        return movies.discoverByGenre(genreId).map {
            it.handleNetworkErrors()
        }
    }
}