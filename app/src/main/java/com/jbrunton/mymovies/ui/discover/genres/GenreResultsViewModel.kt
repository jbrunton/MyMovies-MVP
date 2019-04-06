package com.jbrunton.mymovies.ui.discover.genres

import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.search.SearchState

class GenreResultsViewModel(
        val genreId: String,
        container: Container
) : BaseLoadingViewModel<SearchViewState>(container) {
    val repository: MoviesRepository by inject()
    val viewStateFactory: SearchViewStateFactory by inject()

    override fun start() {
        searchGenre()
    }

    override fun retry() {
        searchGenre()
    }

    private fun searchGenre() {
        subscribe(repository.discoverByGenre(genreId)) {
            viewState.postValue(viewStateFactory.viewState(SearchState.from(it)))
        }
    }
}