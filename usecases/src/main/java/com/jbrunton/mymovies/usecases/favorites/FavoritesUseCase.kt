package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.rx2.asObservable

@ExperimentalCoroutinesApi
class FavoritesUseCase(val movies: MoviesRepository) {
    suspend fun favorites(): DataStream<List<Movie>> {
        return movies.favorites().asObservable()
                .map(this::handleResult)
    }

    private fun handleResult(result: AsyncResult<List<Movie>>): AsyncResult<List<Movie>> {
        return result.handleNetworkErrors()
    }
}
