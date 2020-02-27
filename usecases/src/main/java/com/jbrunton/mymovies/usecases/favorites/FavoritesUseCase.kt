package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import kotlinx.coroutines.flow.map

class FavoritesUseCase(val movies: MoviesRepository) {
    suspend fun favorites(): FlowDataStream<List<Movie>> {
        return movies.favorites()
                .map { handleResult(it) }
    }

    private fun handleResult(result: AsyncResult<List<Movie>>): AsyncResult<List<Movie>> {
        return result.handleNetworkErrors()
    }
}
