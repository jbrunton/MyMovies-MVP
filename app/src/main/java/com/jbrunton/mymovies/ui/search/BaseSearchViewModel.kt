package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.*
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

abstract class BaseSearchViewModel : BaseLoadingViewModel<SearchViewState>() {
    protected fun search(source: () -> DataStream<List<Movie>>) {
        load(source, this::setMoviesResponse)
    }

    protected fun setMoviesResponse(movies: AsyncResult<List<Movie>>) {
        val viewState = movies
                .map(SearchViewStateFactory.Companion::toViewState)
                .handleNetworkErrors()
                .onSuccess {
                    SearchViewStateFactory.errorIfEmpty(it.value)
                }
                .toLoadingViewState(emptyList())
        this.viewState.postValue(viewState)
    }
}