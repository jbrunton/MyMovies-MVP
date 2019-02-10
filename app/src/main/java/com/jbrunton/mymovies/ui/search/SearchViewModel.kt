package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel

open class SearchViewModel(val repository: MoviesRepository) : BaseLoadingViewModel<SearchViewState>() {
    override fun start() {
        viewState.value = SearchViewState.EmptyState
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewState.EmptyState)
        } else {
            subscribe(repository.searchMovies(query)) {
                viewState.postValue(SearchViewState.from(it))
            }
        }
    }
}
