package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.*
import com.jbrunton.async.AsyncResult.Companion.failure
import com.jbrunton.async.AsyncResult.Companion.loading
import com.jbrunton.async.AsyncResult.Companion.success
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import com.jbrunton.usecases.SearchState

class SearchViewStateFactory {
    companion object {
        val NoResultsError = buildError("No Results")
        val EmptyStateError = buildError("Search")
        val EmptyState = AsyncResult.Failure<SearchViewState>(EmptyStateError)
                .toLoadingViewState(SearchViewState.Empty)
        val NoResultsState = AsyncResult.Failure<SearchViewState>(NoResultsError)
                .toLoadingViewState(SearchViewState.Empty)

        fun from(state: SearchState): LoadingViewState<SearchViewState> {
            return when (state) {
                is SearchState.Some -> LoadingViewState.success(SearchViewState.from(state.movies))
                is SearchState.Loading -> LoadingViewState.loading(SearchViewState.Empty)
                is SearchState.NoResults -> NoResultsState
                is SearchState.EmptyQuery -> EmptyState
                is SearchState.Failure -> LoadingViewState.failure(state.error, SearchViewState.Empty)
            }
        }

        private fun buildError(errorMessage: String): LoadingViewStateError {
            return LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false)
        }
    }
}