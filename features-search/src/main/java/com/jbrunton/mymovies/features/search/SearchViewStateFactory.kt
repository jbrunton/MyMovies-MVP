package com.jbrunton.mymovies.features.search

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchResult

class SearchViewStateFactory (private val context: Context) {
    val NoResultsError = searchError(context.getString(R.string.search_no_results))
    val EmptyStateError = searchError(context.getString(R.string.search_empty))

    fun viewState(result: AsyncResult<SearchResult>): LoadingViewState<SearchViewState> {
        return LoadingViewState.build(SearchViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(result: SearchResult): AsyncResult<SearchViewState> = when (result) {
        is SearchResult.Some -> AsyncResult.success(SearchViewState.from(result.movies))
        is SearchResult.NoResults -> AsyncResult.failure(NoResultsError)
        is SearchResult.EmptyQuery -> AsyncResult.failure(EmptyStateError)
    }

    private fun searchError(errorMessage: String): LoadingViewStateError {
        return LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false)
    }
}
