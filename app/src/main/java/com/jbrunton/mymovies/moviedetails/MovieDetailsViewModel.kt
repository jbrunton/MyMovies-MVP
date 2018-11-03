package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

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

    private fun setMovieResponse(movie: Movie) {
        viewState.value = LoadingViewState.Success(MovieViewState(movie))
    }
}
