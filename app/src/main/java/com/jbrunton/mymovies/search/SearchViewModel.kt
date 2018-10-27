package com.jbrunton.mymovies.search

import com.jbrunton.entities.MoviesRepository

open class SearchViewModel(val repository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        viewState.value = SearchViewStateFactory.emptyState
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewStateFactory.emptyState)
        } else {
            search { repository.searchMovies(query) }
        }
    }
}
