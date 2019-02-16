package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.usecases.search.SearchState

class SearchViewStateFactory {
    companion object {
        val NoResultsError = buildError("No Results")
        val EmptyStateError = buildError("Search")

        fun from(result: AsyncResult<SearchState>): LoadingViewState<SearchViewState> {
            return LoadingViewState.build(SearchViewState.Empty).flatMap(result, this::transform)
        }

        private fun transform(state: SearchState): AsyncResult<SearchViewState> = when (state) {
            is SearchState.Some -> AsyncResult.success(SearchViewState.from(state.movies))
            is SearchState.NoResults -> AsyncResult.failure(NoResultsError)
            is SearchState.EmptyQuery -> AsyncResult.failure(EmptyStateError)
        }

        private fun buildError(errorMessage: String): LoadingViewStateError {
            return LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false)
        }
    }
}
