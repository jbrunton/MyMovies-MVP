package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.async.doOnFailure
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase

class FavoritesViewModel(val useCase: FavoritesUseCase) : BaseLoadingViewModel<SearchViewState>() {
    override fun start() {
        loadFavorites()
    }

    override fun retry() {
        loadFavorites()
    }

    private fun loadFavorites() {
        subscribe(useCase.start()) {
            it.doOnFailure(this::showSnackbarIfCachedValue)
            viewState.postValue(SearchViewStateFactory.from(it))
        }
    }
}
