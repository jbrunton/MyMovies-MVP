package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onSuccess
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        private val HiddenState = SearchViewState(emptyList())

        val NoResultsError = buildError("No Results")
        val EmptyStateError = buildError("Search")
        val EmptyState = AsyncResult.Failure<SearchViewState>(EmptyStateError).toLoadingViewState(HiddenState)

        fun from(movies: List<Movie>): SearchViewState {
            return SearchViewState(movies.map(::MovieSearchResultViewState))
        }

        fun map(result: AsyncResult<List<Movie>>): AsyncResult<SearchViewState> {
            return result
                    .map(SearchViewState.Companion::from)
                    .handleNetworkErrors()
                    .onSuccess(this::errorIfEmpty)
        }

        fun from(result: AsyncResult<List<Movie>>): LoadingViewState<SearchViewState> {
            return map(result).toLoadingViewState(HiddenState)
        }

        private fun errorIfEmpty(result: AsyncResult.Success<SearchViewState>): AsyncResult<SearchViewState> {
            return if (result.value.results.isEmpty()) {
                AsyncResult.failure(NoResultsError)
            } else {
                result
            }
        }

        private fun buildError(errorMessage: String): LoadingViewStateError {
            return LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false)
        }
    }
}
