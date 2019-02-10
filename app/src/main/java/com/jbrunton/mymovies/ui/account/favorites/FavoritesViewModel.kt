package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel

class FavoritesViewModel(val moviesRepository: MoviesRepository) : BaseLoadingViewModel<SearchViewState>() {
    override fun start() {
        loadFavorites()
    }

    override fun retry() {
        loadFavorites()
    }

    private fun loadFavorites() {
        subscribe(moviesRepository.favorites()) {
            viewState.postValue(SearchViewStateFactory.from(it))
        }
    }
}
