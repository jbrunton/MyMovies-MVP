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
    }

    override fun retry() {
        loadDetails()
    }

    fun favorite() {
        subscribe(useCase.favorite(), this::onFavorite)
    }

    fun unfavorite() {
        subscribe(useCase.unfavorite(), this::onUnfavorite)
    }

    private fun loadDetails() {
        subscribe(useCase.movie()) {
            viewState.postValue(MovieDetailsViewStateFactory.from(it))
        }
    }

    private fun onFavorite(unit: Unit) {
        val message = SnackbarMessage(
                "Added to favorites",
                "Undo",
                { unfavorite() }
        )
        snackbar.postValue(message)
        loadDetails()
    }

    private fun onUnfavorite(unit: Unit) {
        val message = SnackbarMessage(
                "Removed from favorites",
                "Undo",
                { favorite() }
        )
        snackbar.postValue(message)
        loadDetails()
    }
}
