package com.jbrunton.mymovies.search

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.Loading
import com.jbrunton.mymovies.shared.LoadingViewState

open class SearchViewModel(private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<SearchViewState>>()

    override fun start() {
        viewState.value = SearchViewStateFactory.emptyState
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(SearchViewStateFactory.emptyState)
        } else {
            viewState.postValue(Loading())
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setMoviesResponse, this::setErrorResponse)
        }
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.postValue(SearchViewStateFactory.fromList(movies))
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.postValue(LoadingViewState.fromError(throwable))
    }
}
