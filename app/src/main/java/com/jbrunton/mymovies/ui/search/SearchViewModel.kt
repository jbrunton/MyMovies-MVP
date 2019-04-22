package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.libs.ui.BaseLoadingViewModel
import com.jbrunton.libs.ui.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchUseCase

class SearchViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: SearchUseCase by inject()
    val viewStateFactory: SearchViewStateFactory by inject()

    override fun start() {
        super.start()
        subscribe(useCase.results()) {
            viewState.postValue(viewStateFactory.viewState(it))
        }
    }

    fun onSearchQueryChanged(query: String) {
        useCase.search(query)
    }

    fun onMovieSelected(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }
}
