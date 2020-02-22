package com.jbrunton.mymovies.ui.moviedetails

import androidx.lifecycle.viewModelScope
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.SnackbarEvent
import com.jbrunton.mymovies.usecases.moviedetails.FavoriteResult
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsUseCase
import kotlinx.coroutines.launch

class MovieDetailsViewModel(val movieId: String, container: Container) :
        BaseLoadingViewModel<MovieDetailsViewState>(container)
{
    val useCase: MovieDetailsUseCase by inject()
    val viewStateFactory: MovieDetailsViewStateFactory by inject()

    override fun start() {
        super.start()
        loadDetails()
    }

    fun onRetryClicked() {
        loadDetails()
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            subscribe(useCase.favorite(movieId)) {
                it.doOnSuccess {
                    handleFavoriteResult(it, viewStateFactory.FavoriteAddedEvent(this@MovieDetailsViewModel::onUnfavoriteClicked))
                }
            }
        }
    }

    fun onUnfavoriteClicked() {
        viewModelScope.launch {
            subscribe(useCase.unfavorite(movieId)) {
                it.doOnSuccess {
                    handleFavoriteResult(it, viewStateFactory.FavoriteRemovedEvent(this@MovieDetailsViewModel::onUnfavoriteClicked))
                }
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
