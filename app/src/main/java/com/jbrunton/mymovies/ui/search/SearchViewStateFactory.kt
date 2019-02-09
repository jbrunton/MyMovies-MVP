package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onSuccess
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

class SearchViewStateFactory {
    companion object {
        val NoResults = buildEmptyState("No Results")
        val EmptyState = buildEmptyState("Search")
                .toLoadingViewState(SearchViewState.Empty)

        fun from(result: AsyncResult<List<Movie>>): LoadingViewState<SearchViewState> {
            return result
                    .map(SearchViewState.Companion::toViewState)
                    .handleNetworkErrors()
                    .onSuccess {
                        errorIfEmpty(it.value)
                    }
                    .toLoadingViewState(SearchViewState.Empty)
        }

        private fun errorIfEmpty(movies: SearchViewState): AsyncResult<SearchViewState> {
            return if (movies.results.isEmpty()) {
                NoResults
            } else {
                AsyncResult.Success(movies)
            }
        }

        private fun buildEmptyState(errorMessage: String): AsyncResult.Failure<SearchViewState> {
            return AsyncResult.Failure(LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false))
        }
    }
}