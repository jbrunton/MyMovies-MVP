package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.models.Movie

sealed class SearchResult {
    object EmptyQuery : SearchResult()
    object NoResults : SearchResult()
    data class Some(val movies: List<Movie>) : SearchResult()

    companion object {
        fun from(result: AsyncResult<List<Movie>>): AsyncResult<SearchResult> {
            return result.map {
                if (it.isEmpty()) {
                    NoResults
                } else {
                    Some(it)
                }
            }
        }
    }
}
