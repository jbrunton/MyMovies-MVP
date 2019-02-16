package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie

sealed class SearchState {
    object EmptyQuery : SearchState()
    object NoResults : SearchState()
    data class Some(val movies: List<Movie>) : SearchState()

    companion object {
        fun from(result: AsyncResult<List<Movie>>): AsyncResult<SearchState> {
            return result.handleNetworkErrors().map {
                if (it.isEmpty()) {
                    NoResults
                } else {
                    Some(it)
                }
            }
        }
    }
}
