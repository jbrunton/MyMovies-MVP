package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

open class SearchViewModel(val repository: MoviesRepository) : BaseLoadingViewModel<SearchViewState>() {
    override fun start() {
        viewState.value = SearchViewStateFactory.emptyState
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewStateFactory.emptyState)
        } else {
            viewState.postValue(LoadingViewState.Loading)
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setMoviesResponse, this::setErrorResponse)
        }
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.postValue(SearchViewStateFactory.fromList(movies))
    }
}
