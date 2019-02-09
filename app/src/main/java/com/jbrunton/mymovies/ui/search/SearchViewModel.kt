package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

open class SearchViewModel(val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        viewState.value = SearchViewState.emptyState.toLoadingViewState(SearchViewState.Empty)
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewState.emptyState.toLoadingViewState(SearchViewState.Empty))
        } else {
            search { repository.searchMovies(query) }
        }
    }
}
