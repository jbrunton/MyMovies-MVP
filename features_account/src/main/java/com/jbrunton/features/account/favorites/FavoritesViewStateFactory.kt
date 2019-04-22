package com.jbrunton.features.account.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.libs.ui.LoadingViewState
import com.jbrunton.mymovies.shared.ui.SearchViewState

object FavoritesViewStateFactory {
    fun viewState(result: AsyncResult<List<Movie>>): LoadingViewState<SearchViewState> {
        return LoadingViewState.build(SearchViewState.Empty).map(result) { SearchViewState.from(it) }
    }
}
