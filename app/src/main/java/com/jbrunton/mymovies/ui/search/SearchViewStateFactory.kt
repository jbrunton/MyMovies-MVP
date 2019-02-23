package com.jbrunton.mymovies.ui.search

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.usecases.search.SearchState

class SearchViewStateFactory (private val context: Context) {
    val NoResultsError = searchError(context.getString(R.string.search_no_results))
    val EmptyStateError = searchError(context.getString(R.string.search_empty))
    
    fun viewState(result: AsyncResult<SearchState>): LoadingViewState<SearchViewState> {
        return LoadingViewState.build(SearchViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(state: SearchState): AsyncResult<SearchViewState> = when (state) {
        is SearchState.Some -> AsyncResult.success(SearchViewState.from(state.movies))
        is SearchState.NoResults -> AsyncResult.failure(NoResultsError)
        is SearchState.EmptyQuery -> AsyncResult.failure(EmptyStateError)
    }

    private fun searchError(errorMessage: String): LoadingViewStateError {
        return LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false)
    }
}
