package com.jbrunton.mymovies.moviedetails

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.shared.BaseViewModel

class MovieDetailsViewModel(private val movieId: String, private val repository: MoviesRepository) : BaseViewModel() {
    private val viewState = MutableLiveData<MovieDetailsViewState>()
    private val viewStateFactory = MovieDetailsViewStateFactory()

    fun viewState(): LiveData<MovieDetailsViewState> {
        return viewState
    }

    override fun start() {
        loadDetails()
    }

    fun retry() {
        loadDetails()
    }

    private fun loadDetails() {
        viewState.setValue(viewStateFactory.loadingState())
        launchSafely({ setErrorResponse(it) }) {
            setMovieResponse(repository.getMovie(movieId))
        }
    }

    private fun setMovieResponse(movie: Movie) {
        viewState.value = viewStateFactory.fromMovie(movie)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = viewStateFactory.fromError(throwable)
    }

    class Factory(
            private val movieId: String,
            private val repository: MoviesRepository
    ) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MovieDetailsViewModel(movieId, repository) as T
        }
    }
}
