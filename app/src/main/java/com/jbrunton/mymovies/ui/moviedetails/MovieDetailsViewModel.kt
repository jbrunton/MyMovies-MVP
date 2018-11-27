package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.*

class MovieDetailsViewModel(val movieId: String, val repository: MoviesRepository) : BaseLoadingViewModel<MovieViewState>() {
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
                .doOnNetworkError(this::showSnackbarIfCachedValue)
                .handleNetworkErrors()
                .toLoadingViewState(defaultViewState)
    }

    private val defaultViewState = MovieViewState(Movie.emptyMovie)
}
