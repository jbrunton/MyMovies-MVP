package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.usecases.search.SearchState

class FavoritesUseCase(val movies: MoviesRepository) {
    fun favorites(): DataStream<SearchState> {
        return movies.favorites()
                .map(this::handleResult)
    }

    private fun handleResult(result: AsyncResult<List<Movie>>): AsyncResult<SearchState> {
        return SearchState.from(result)
                .handleNetworkErrors()
    }
}