package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.search.SearchUseCase

class SearchViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: SearchUseCase by inject()

    override fun start() {
        subscribe(useCase.results) {
            viewState.postValue(SearchViewStateFactory.viewState(it))
        }
        useCase.start(schedulerContext)
    }

    fun performSearch(query: String) {
        useCase.search(query)
    }
}
