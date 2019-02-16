package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnFailure
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SnackbarMessage
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

class MovieDetailsViewModel(
        val movieId: String,
        val repository: MoviesRepository,
        val preferences: ApplicationPreferences
) : BaseLoadingViewModel<MovieViewState>() {
    override fun start() {
        loadDetails()
    }

    override fun retry() {
        loadDetails()
    }

    fun favorite() {
        subscribe(repository.favorite(movieId), this::onFavorite)
    }

    fun unfavorite() {
        subscribe(repository.unfavorite(movieId), this::onUnfavorite)
    }

    private fun loadDetails() {
        subscribe(repository.getMovie(movieId), this::setMovieResponse)
    }

    private fun setMovieResponse(state: AsyncResult<Movie>) {
        viewState.value = state
                .map {
                    val favorite = preferences.favorites?.contains(movieId) ?: false
                    MovieViewState.from(it, favorite)
                }
                .doOnFailure(this::showSnackbarIfCachedValue)
                .handleNetworkErrors()
                .toLoadingViewState(MovieViewState.Empty)
    }

    private fun onFavorite(any: Any) {
        val message = SnackbarMessage(
                "Added to favorites",
                "Undo",
                { unfavorite() }
        )
        snackbar.postValue(message)
        loadDetails()
    }

    private fun onUnfavorite(any: Any) {
        val message = SnackbarMessage(
                "Removed from favorites",
                "Undo",
                { favorite() }
        )
        snackbar.postValue(message)
        loadDetails()
    }
}
