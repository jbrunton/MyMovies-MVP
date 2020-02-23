package com.jbrunton.mymovies.features.search

import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import org.koin.core.Koin
import org.koin.core.inject

class SearchViewModel(
        val useCase: SearchUseCase,
        val viewStateFactory: SearchViewStateFactory,
        navigator: Navigator,
        schedulerFactory: SchedulerFactory
) : BaseLoadingViewModel<SearchViewState>(navigator, schedulerFactory) {
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
