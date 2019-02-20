package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SnackbarMessage
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase

class MovieDetailsViewModel(val movieId: String, container: Container) :
        BaseLoadingViewModel<MovieDetailsViewState>(container)
{
    val useCase: MovieDetailsUseCase by inject { parametersOf(movieId, schedulerContext) }

    override fun start() {
        subscribe(useCase.movie) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }

        subscribe(useCase.favoriteAddedSnackbar, this::showFavoriteAddedSnackbar)
        subscribe(useCase.favoriteRemovedSnackbar, this::showFavoriteRemovedSnackbar)
        subscribe(useCase.signedOutSnackbar, this::showSignedOutSnackbar)

        useCase.start()
    }

    override fun retry() {
        useCase.start()
    }

    fun favorite() {
        useCase.favorite()
    }

    fun unfavorite() {
        useCase.unfavorite()
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
