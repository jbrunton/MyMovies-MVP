package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors

sealed class SearchState {
    object EmptyQuery : SearchState()
    object NoResults : SearchState()
    data class Some(val movies: List<Movie>) : SearchState()

    companion object {
        fun from(result: AsyncResult<List<Movie>>): AsyncResult<SearchState> {
            return result.handleNetworkErrors().map {
                if (it.isEmpty()) {
                    SearchState.NoResults
                } else {
                    SearchState.Some(it)
                }
            }
        }
    }
}
