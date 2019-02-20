package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import io.reactivex.subjects.PublishSubject

open class SearchViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: SearchUseCase by inject { parametersOf(schedulerContext) }
    private val searches = PublishSubject.create<String>()

    override fun start() {
        subscribe(useCase.reduce(searches)) {
            viewState.postValue(SearchViewStateFactory.from(it))
        }
    }

    open fun performSearch(query: String) {
        searches.onNext(query)
    }
}
