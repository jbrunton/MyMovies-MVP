package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.usecases.search.SearchState

class GenreResultsViewModel(
        val genreId: String,
        val repository: MoviesRepository
) : BaseLoadingViewModel<SearchViewState>() {

    override fun start() {
        searchGenre()
    }

    override fun retry() {
        searchGenre()
    }

    private fun searchGenre() {
        subscribe(repository.discoverByGenre(genreId)) {
            viewState.postValue(SearchViewStateFactory.from(SearchState.from(it)))
        }
    }
}
