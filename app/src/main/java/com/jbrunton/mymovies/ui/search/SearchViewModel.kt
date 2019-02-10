package com.jbrunton.mymovies.ui.search

import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import io.reactivex.subjects.PublishSubject

open class SearchViewModel(val useCase: SearchUseCase) : BaseLoadingViewModel<SearchViewState>() {
    private val searches = PublishSubject.create<String>()

    init {
        subscribe(useCase.search(searches)) {
            viewState.postValue(it)
        }
    }

    override fun start() {
        viewState.value = SearchViewStateFactory.EmptyState
    }

    open fun performSearch(query: String) {
        searches.onNext(query)
//        if (query.isEmpty()) {
//            viewState.postValue(SearchViewStateFactory.EmptyState)
//        } else {
//            useCase.search(query)
//        }
    }
}
