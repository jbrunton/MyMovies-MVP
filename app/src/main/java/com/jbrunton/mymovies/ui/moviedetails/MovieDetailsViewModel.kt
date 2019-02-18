package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SnackbarMessage
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase

class MovieDetailsViewModel(
        val movieId: String,
        override val container: Container
) : BaseLoadingViewModel<MovieDetailsViewState>(), HasContainer {
    val useCase: MovieDetailsUseCase by inject { parametersOf(movieId) }

    override fun start() {
        loadDetails()
        subscribe(useCase.favoriteAddedSnackbar, this::showFavoriteAddedSnackbar)
        subscribe(useCase.favoriteRemovedSnackbar, this::showFavoriteRemovedSnackbar)
        subscribe(useCase.signedOutSnackbar, this::showSignedOutSnackbar)
    }

    override fun retry() {
        loadDetails()
    }

    fun favorite() {
        subscribe(useCase.favorite()) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }
    }

    fun unfavorite() {
        subscribe(useCase.unfavorite()) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }
    }

    private fun loadDetails() {
        subscribe(useCase.movie()) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }
    }

    private fun showFavoriteAddedSnackbar(unit: Unit) {
        val message = SnackbarMessage(
                "Added to favorites",
                "Undo",
                { unfavorite() }
        )
        snackbar.postValue(message)
    }

    private fun showFavoriteRemovedSnackbar(unit: Unit) {
        val message = SnackbarMessage(
                "Removed from favorites",
                "Undo",
                { favorite() }
        )
        snackbar.postValue(message)
    }

    private fun showSignedOutSnackbar(unit: Unit) {
        val message = SnackbarMessage(
                "Sign in to add favorites",
                "OK"
        )
        snackbar.postValue(message)
    }
}
