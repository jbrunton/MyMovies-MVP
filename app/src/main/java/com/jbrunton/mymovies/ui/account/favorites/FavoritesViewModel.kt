package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase

class FavoritesViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: FavoritesUseCase by inject()
    val viewStateFactory: SearchViewStateFactory by inject()

    override fun start() {
        subscribe(useCase.retrySnackbar) {
            snackbar.postValue(NetworkErrorSnackbar(retry = true))
        }
        subscribe(useCase.favorites) {
            viewState.postValue(viewStateFactory.viewState(it))
        }
        useCase.start(schedulerContext)
    }

    override fun retry() {
        useCase.retry()
    }
}
