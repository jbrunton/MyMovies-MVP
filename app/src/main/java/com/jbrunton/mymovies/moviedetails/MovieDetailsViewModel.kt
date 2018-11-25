package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.*

class MovieDetailsViewModel(val movieId: String, val repository: MoviesRepository) : BaseLoadingViewModel<MovieViewState>() {
    val showRetrySnackbar = SingleLiveEvent<Unit>()

    override fun start() {
        loadDetails()
    }

    fun retry() {
        loadDetails()
    }

    private fun loadDetails() {
        load({
            repository.getMovie(movieId)
        }, this::setMovieResponse)
    }

    private fun setMovieResponse(state: AsyncResult<Movie>) {
        viewState.value = state
                .map { MovieViewState(it) }
                .doOnNetworkErrorWithCachedValue {
                    showRetrySnackbar.call()
                }
                .handleNetworkErrors()
                .toLoadingViewState(defaultViewState)
    }

    private val defaultViewState = MovieViewState(Movie.emptyMovie)
}
