package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.map
import com.jbrunton.mymovies.shared.toViewState

abstract class BaseSearchViewModel : BaseLoadingViewModel<SearchViewState>() {
    protected fun search(source: () -> DataStream<List<Movie>>) {
        load(source, this::setMoviesResponse)
    }

    protected fun setMoviesResponse(movies: LoadingState<List<Movie>>) {
        val viewState = movies
                .toViewState(this::convertToViewState)
                .map(this::errorIfEmpty)
        this.viewState.postValue(viewState)
    }

    private fun convertToViewState(movies: List<Movie>) =
            movies.map { MovieSearchResultViewState(it) }

    private fun errorIfEmpty(movies: SearchViewState) =
            if (movies.isEmpty()) {
                SearchViewStateFactory.errorNoResults
            } else {
                LoadingViewState.Success(movies)
            }
}