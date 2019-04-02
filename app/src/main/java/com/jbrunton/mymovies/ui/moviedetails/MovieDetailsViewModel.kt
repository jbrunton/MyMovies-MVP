package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsSnackbar
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase

class MovieDetailsViewModel(val movieId: String, container: Container) :
        BaseLoadingViewModel<MovieDetailsViewState>(container)
{
    val useCase: MovieDetailsUseCase by inject { parametersOf(movieId) }
    val viewStateFactory: MovieDetailsViewStateFactory by inject()

    override fun start() {
        super.start()
        subscribe(useCase.movie) {
            viewState.postValue(viewStateFactory.viewState(it))
        }

        subscribe(useCase.snackbar, this::showSnackbar)

        useCase.start(schedulerContext)
    }

    fun onRetryClicked() {
        useCase.retry()
    }

    fun onFavoriteClicked() {
        useCase.favorite()
    }

    fun onUnfavoriteClicked() {
        useCase.unfavorite()
    }

    private fun showSnackbar(state: MovieDetailsSnackbar) {
        val message = viewStateFactory.snackbar(state, this::onUnfavoriteClicked, this::onFavoriteClicked)
        snackbar.postValue(message)
    }
}
