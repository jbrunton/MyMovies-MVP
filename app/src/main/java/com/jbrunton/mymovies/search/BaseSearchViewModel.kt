package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.handleNetworkErrors
import com.jbrunton.mymovies.shared.toLoadingViewState

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