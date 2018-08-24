package com.jbrunton.mymovies.search

import android.arch.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.shared.BaseViewModel

open class SearchViewModel(private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<SearchViewState>()
    private val viewStateFactory = SearchViewStateFactory()

    override fun start() {
        viewState.value = viewStateFactory.searchEmptyState
    }

    open fun performSearch(query: String) {
        if (query.isEmpty()) {
            viewState.postValue(viewStateFactory.searchEmptyState)
        } else {
            viewState.postValue(viewStateFactory.loadingState)
            repository.searchMovies(query)
                    .compose(applySchedulers())
                    .subscribe(this::setMoviesResponse, this::setErrorResponse)
        }
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.postValue(viewStateFactory.fromList(movies))
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.postValue(viewStateFactory.fromError(throwable))
    }
}
