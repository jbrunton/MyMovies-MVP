package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverState

class DiscoverViewStateFactory {
    companion object {
        fun from(result: AsyncResult<DiscoverState>): LoadingViewState<DiscoverViewState> {
            return LoadingViewState.build(DiscoverViewState.Empty).map(result) {
                DiscoverViewState(
                        it.nowPlaying.map { MovieSearchResultViewState(it) },
                        it.nowPlaying.map { MovieSearchResultViewState(it) },
                        it.genres
                )
            }
        }
    }
}