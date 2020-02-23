package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.SnackbarEvent
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.usecases.moviedetails.FavoriteResult
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import org.koin.core.Koin
import org.koin.core.inject

class MovieDetailsViewModel(
        val movieId: String,
        val useCase: MovieDetailsUseCase,
        val viewStateFactory: MovieDetailsViewStateFactory,
        navigator: Navigator,
        schedulerFactory: SchedulerFactory
) : BaseLoadingViewModel<MovieDetailsViewState>(navigator, schedulerFactory) {

    override fun start() {
        super.start()
        loadDetails()
    }

    fun onRetryClicked() {
        loadDetails()
    }

    fun onFavoriteClicked() {
        subscribe(useCase.favorite(movieId)) {
            it.doOnSuccess {
                handleFavoriteResult(it, viewStateFactory.FavoriteAddedEvent(this::onUnfavoriteClicked))
            }
        }
    }

    fun onUnfavoriteClicked() {
        subscribe(useCase.unfavorite(movieId)) {
            it.doOnSuccess {
                handleFavoriteResult(it, viewStateFactory.FavoriteRemovedEvent(this::onUnfavoriteClicked))
            }
        }
    }

    private fun loadDetails() {
        subscribe(useCase.details(movieId)) {
            viewState.postValue(viewStateFactory.viewState(it))
        }
    }

    private fun handleFavoriteResult(
            result: AsyncResult.Success<FavoriteResult>,
            successSnackbar: SnackbarEvent
    ) = when (result.value) {
        FavoriteResult.Success -> {
            snackbar.postValue(successSnackbar)
            loadDetails()
        }
        FavoriteResult.SignedOut -> snackbar.postValue(viewStateFactory.SignedOutEvent)
    }
}
