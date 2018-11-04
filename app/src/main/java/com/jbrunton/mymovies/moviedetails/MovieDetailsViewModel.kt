package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.models.map
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.BaseLoadingViewModel

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

    private fun setMovieResponse(state: LoadingState<Movie>) {
        viewState.value = state.map { MovieViewState(it) }
    }
}
