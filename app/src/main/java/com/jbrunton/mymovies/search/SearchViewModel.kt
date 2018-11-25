package com.jbrunton.mymovies.search

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.shared.toLoadingViewState

open class SearchViewModel(val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        viewState.value = SearchViewStateFactory.emptyState.toLoadingViewState(emptyList())
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewStateFactory.emptyState.toLoadingViewState(emptyList()))
        } else {
            search { repository.searchMovies(query) }
        }
    }
}
