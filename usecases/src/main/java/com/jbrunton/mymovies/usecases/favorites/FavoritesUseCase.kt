package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.usecases.search.SearchState

class FavoritesUseCase(
        val movies: MoviesRepository
) {
    fun reduce(): DataStream<SearchState> {
        return movies.favorites().map {
            SearchState.from(it).handleNetworkErrors()
        }
    }
}