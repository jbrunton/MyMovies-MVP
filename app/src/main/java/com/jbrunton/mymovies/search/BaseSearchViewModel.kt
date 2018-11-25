package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.*
import com.jbrunton.mymovies.shared.BaseLoadingViewModel

abstract class BaseSearchViewModel : BaseLoadingViewModel<SearchViewState>() {
    protected fun search(source: () -> DataStream<List<Movie>>) {
        load(source, this::setMoviesResponse)
    }

    protected fun setMoviesResponse(movies: AsyncResult<List<Movie>>) {
        val viewState = movies
                .map(SearchViewStateFactory.Companion::toViewState)
                .onSuccess {
                    SearchViewStateFactory.errorIfEmpty(it.value)
                }
        this.viewState.postValue(viewState)
    }
}