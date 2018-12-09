package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.search.BaseSearchViewModel

class FavoritesViewModel(val moviesRepository: MoviesRepository) : BaseSearchViewModel() {
    override fun start() {
        loadFavorites()
    }

    fun retry() {
        loadFavorites()
    }

    private fun loadFavorites() {
        search {
            moviesRepository.favorites()
        }
    }
}
