package com.jbrunton.mymovies.ui.search

import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.search.SearchUseCase
import io.reactivex.subjects.PublishSubject

open class SearchViewModel(val useCase: SearchUseCase) : BaseLoadingViewModel<SearchViewState>() {
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
