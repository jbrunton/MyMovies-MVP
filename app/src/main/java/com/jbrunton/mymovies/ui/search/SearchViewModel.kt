package com.jbrunton.mymovies.ui.search

import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import io.reactivex.subjects.PublishSubject

open class SearchViewModel(val useCase: SearchUseCase) : BaseLoadingViewModel<SearchViewState>() {
    private val searches = PublishSubject.create<String>()

    override fun start() {
        subscribe(useCase.start(searches)) {
            viewState.postValue(it)
        }
    }

    open fun performSearch(query: String) {
        searches.onNext(query)
    }
}
