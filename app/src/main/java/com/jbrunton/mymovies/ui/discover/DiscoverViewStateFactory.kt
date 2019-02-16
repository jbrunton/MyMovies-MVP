package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.flatMap
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverState

class DiscoverViewStateFactory {
    companion object {
        fun from(result: AsyncResult<DiscoverState>): LoadingViewState<DiscoverViewState> {
            return result
                    .flatMap(this::toViewState)
                    .toLoadingViewState(DiscoverViewState.Empty)
        }

        private fun toViewState(state: DiscoverState): AsyncResult<DiscoverViewState> =
                AsyncResult.success(DiscoverViewState(
                        state.nowPlaying.map { MovieSearchResultViewState(it) },
                        state.nowPlaying.map { MovieSearchResultViewState(it) },
                        state.genres
                ))
    }
}