package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

class MovieDetailsViewModel(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) : BaseLoadingViewModel<MovieViewState>() {
    override fun start() {
        loadDetails()
    }

    fun retry() {
        loadDetails()
    }

    fun favorite() {
        repository.favorite(movieId)
                .compose(applySchedulers())
                .subscribe {
                    loadDetails()
                }
    }

    fun unfavorite() {
        repository.unfavorite(movieId)
                .compose(applySchedulers())
                .subscribe {
                    loadDetails()
                }
    }

    private fun loadDetails() {
        load({
            repository.getMovie(movieId)
        }, this::setMovieResponse)
    }

    private fun setMovieResponse(state: AsyncResult<Movie>) {
        viewState.value = state
                .map {
                    val favorite = (preferences.favorites ?: emptySet()).contains(movieId)
                    MovieViewState.from(it, favorite)
                }
                .handleNetworkErrors()
                .toLoadingViewState(MovieViewState.Empty)
    }
}
