package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.search.SearchUseCase

class SearchViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: SearchUseCase by inject { parametersOf(schedulerContext) }

    override fun start() {
        subscribe(useCase.results) {
            viewState.postValue(SearchViewStateFactory.from(it))
        }
        useCase.start()
    }

    fun performSearch(query: String) {
        useCase.search(query)
    }
}
