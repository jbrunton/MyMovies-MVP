package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SnackbarMessage
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsSnackbar
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase

class MovieDetailsViewModel(val movieId: String, container: Container) :
        BaseLoadingViewModel<MovieDetailsViewState>(container)
{
    val useCase: MovieDetailsUseCase by inject { parametersOf(movieId, schedulerContext) }

    override fun start() {
        subscribe(useCase.movie) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }

        subscribe(useCase.snackbar, this::showSnackbar)

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

    private val FavoriteAdded = SnackbarMessage(
            "Added to favorites",
            "Undo",
            { unfavorite() })

    private val FavoriteRemoved = SnackbarMessage(
            "Added to favorites",
            "Undo",
            { unfavorite() })

    private val SignedOut = SnackbarMessage(
            "Sign in to add favorites",
            "OK"
    )

    private fun showSnackbar(state: MovieDetailsSnackbar) {
        val message = when (state) {
            is MovieDetailsSnackbar.FavoriteAdded -> FavoriteAdded
            is MovieDetailsSnackbar.FavoriteRemoved -> FavoriteRemoved
            is MovieDetailsSnackbar.SignedOut -> SignedOut
        }
        snackbar.postValue(message)
    }
}
