package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

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
                .map { MovieViewState.from(it, false) }
                .handleNetworkErrors()
                .toLoadingViewState(MovieViewState.Empty)
    }
}
