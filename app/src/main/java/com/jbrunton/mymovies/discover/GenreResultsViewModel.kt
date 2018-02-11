package com.jbrunton.mymovies.discover

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.networking.DescriptiveError

class GenreResultsViewModel(private val genreId: String, private val repository: MoviesRepository) : BaseViewModel() {
    private val viewState = MutableLiveData<SearchViewState>()
    private val viewStateFactory = SearchViewStateFactory()

    fun viewState(): LiveData<SearchViewState> {
        return viewState
    }

    override fun start() {
        viewState.setValue(
                SearchViewState(
                        LoadingViewState.LOADING_STATE,
                        emptyList()))
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = viewStateFactory.fromList(movies)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = viewStateFactory.fromError(DescriptiveError.from(throwable))
    }

    class Factory(private val genreId: String, private val repository: MoviesRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return GenreResultsViewModel(genreId, repository) as T
        }
    }
}