package com.jbrunton.mymovies.moviedetails

import com.google.common.base.Optional
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.SingleLiveEvent
import com.jbrunton.mymovies.shared.handleNetworkErrors
import com.jbrunton.mymovies.shared.toLoadingViewState

class MovieDetailsViewModel(val movieId: String, val repository: MoviesRepository) : BaseLoadingViewModel<MovieViewState>() {
    val showSnackbar = SingleLiveEvent<Unit>()

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
                .handleNetworkErrors()
                .onFailure {
                    if (it.cachedValue != null) {
                        showSnackbar.call()
                    }
                    it
                }
                .toLoadingViewState(defaultViewState)
    }

    private val defaultViewState = MovieViewState(Movie.emptyMovie)
}
