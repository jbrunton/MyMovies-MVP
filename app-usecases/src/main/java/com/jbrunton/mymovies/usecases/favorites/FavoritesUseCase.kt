package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository

class FavoritesUseCase(val movies: MoviesRepository) {
    fun favorites(): DataStream<List<Movie>> {
        return movies.favorites()
                .map(this::handleResult)
    }

    private fun handleResult(result: AsyncResult<List<Movie>>): AsyncResult<List<Movie>> {
        return result.handleNetworkErrors()
    }
}