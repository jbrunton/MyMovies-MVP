package com.jbrunton.mymovies.discover

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class DiscoverViewModel internal constructor(private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<SearchViewState>>()

    override fun start() {
        viewState.setValue(LoadingViewState.Loading)
        repository.nowPlaying()
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = SearchViewStateFactory.fromList(movies)
    }

    private fun setErrorResponse(error: Throwable) {
        viewState.value = LoadingViewState.fromError(error)
    }
}
