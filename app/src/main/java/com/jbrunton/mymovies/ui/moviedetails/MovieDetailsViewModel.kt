package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SnackbarMessage
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase

class MovieDetailsViewModel(
        val movieId: String,
        override val container: Container
) : BaseLoadingViewModel<MovieViewState>(), HasContainer {
    val useCase: MovieDetailsUseCase by inject { parametersOf(movieId) }

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
        subscribe(useCase.movie(movieId), this::setMovieResponse)
    }

    private fun setMovieResponse(state: AsyncResult<Movie>) {
        viewState.value = state
                .map {
                    val favorite = preferences.favorites.contains(movieId)
                    MovieViewState.from(it, favorite)
                }
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
