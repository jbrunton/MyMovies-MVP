package com.jbrunton.mymovies.features.search

import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import org.koin.core.Koin
import org.koin.core.inject

class SearchViewModel(koin: Koin) : BaseLoadingViewModel<SearchViewState>(koin) {
    val useCase: SearchUseCase by koin.inject()
    val viewStateFactory: SearchViewStateFactory by koin.inject()

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
