package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.onSuccess
import com.jbrunton.mymovies.shared.toViewState

abstract class BaseSearchViewModel : BaseLoadingViewModel<SearchViewState>() {
    protected fun search(source: () -> DataStream<List<Movie>>) {
        load(source, this::setMoviesResponse)
    }

    protected fun setMoviesResponse(movies: LoadingState<List<Movie>>) {
        val viewState = movies
                .toViewState(SearchViewStateFactory.Companion::toViewState)
                .onSuccess {
                    SearchViewStateFactory.errorIfEmpty(it.value)
                }
        this.viewState.postValue(viewState)
    }
}