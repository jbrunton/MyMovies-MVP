package com.jbrunton.mymovies.features.search

import androidx.lifecycle.viewModelScope
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SearchViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: SearchUseCase by inject()
    val viewStateFactory: SearchViewStateFactory by inject()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun start() {
        super.start()
        viewModelScope.launch {
            useCase.results().collect {
                viewState.postValue(viewStateFactory.viewState(it))
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun onSearchQueryChanged(query: String) {
        useCase.search(query)
    }

    fun onMovieSelected(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }
}
