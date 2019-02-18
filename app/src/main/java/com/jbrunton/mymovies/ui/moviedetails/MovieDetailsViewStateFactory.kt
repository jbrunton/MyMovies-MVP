package com.jbrunton.mymovies.ui.moviedetails

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.usecases.moviedetails.MovieDetailsState

object MovieDetailsViewStateFactory {
    fun from(result: AsyncResult<MovieDetailsState>): LoadingViewState<MovieDetailsViewState> {
        return LoadingViewState.build(MovieDetailsViewState.Empty).map(result) {
            MovieDetailsViewState.from(it.movie, it.favorite)
        }
    }
}