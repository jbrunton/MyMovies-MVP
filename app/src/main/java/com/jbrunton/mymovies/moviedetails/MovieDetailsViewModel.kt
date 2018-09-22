package com.jbrunton.mymovies.moviedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
        repository.getMovie(movieId)
                .compose(applySchedulers())
                .subscribe(this::setMovieResponse, this::setErrorResponse)
    }

    private fun setMovieResponse(movie: Movie) {
        viewState.value = viewStateFactory.fromMovie(movie)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = viewStateFactory.fromError(throwable)
    }
}
