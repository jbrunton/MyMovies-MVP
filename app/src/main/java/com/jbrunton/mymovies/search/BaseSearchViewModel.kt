package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import io.reactivex.Observable

abstract class BaseSearchViewModel : BaseLoadingViewModel<SearchViewState>() {
    protected fun search(source: () -> Observable<List<Movie>>) {
        load(source, this::setMoviesResponse)
    }

    protected fun setMoviesResponse(movies: List<Movie>) {
        viewState.postValue(SearchViewStateFactory.fromList(movies))
    }
}