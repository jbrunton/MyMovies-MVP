package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.*
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@UseExperimental // for purposes of the combine operator
class DiscoverUseCase(
        val movies: MoviesRepository,
        val genres: GenresRepository
) {
    suspend fun discover(): FlowDataStream<DiscoverState> {
        return combine(
                movies.nowPlaying(),
                movies.popular(),
                genres.genres())
        { nowPlaying, popular, genres ->
            AsyncResult.zip(nowPlaying, popular, genres, ::DiscoverState)
                    .handleNetworkErrors()

        }
    }

    suspend fun discoverByGenre(genreId: String): FlowDataStream<List<Movie>> {
        return movies.discoverByGenre(genreId).map { it.handleNetworkErrors() }
    }
}
