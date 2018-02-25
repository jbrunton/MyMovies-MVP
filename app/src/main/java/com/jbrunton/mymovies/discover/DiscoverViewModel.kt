package com.jbrunton.mymovies.discover

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class DiscoverViewModel internal constructor(private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<SearchViewState>()
    private val viewStateFactory = SearchViewStateFactory()

    override fun start() {
        viewState.setValue(
                SearchViewState(
                        LoadingViewState.LOADING_STATE,
                        emptyList()))
        repository.nowPlaying()
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = viewStateFactory.fromList(movies)
    }

    private fun setErrorResponse(error: Throwable) {
        viewState.value = viewStateFactory.fromError(error)
    }

    class Factory(private val repository: MoviesRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return DiscoverViewModel(repository) as T
        }
    }
}
