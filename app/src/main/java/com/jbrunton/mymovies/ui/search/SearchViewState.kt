package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        val Empty = SearchViewState(emptyList())

        val errorNoResults = buildEmptyState("No Results")
        val emptyState = buildEmptyState("Search")

        fun toViewState(movies: List<Movie>): SearchViewState {
            return SearchViewState(movies.map { MovieSearchResultViewState(it) })
        }

        fun errorIfEmpty(movies: SearchViewState): AsyncResult<SearchViewState> {
            return if (movies.results.isEmpty()) {
                SearchViewState.errorNoResults
            } else {
                AsyncResult.Success(movies)
            }
        }

        private fun buildEmptyState(errorMessage: String): AsyncResult.Failure<SearchViewState> {
            return AsyncResult.Failure(LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false))
        }
    }
}
