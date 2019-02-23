package com.jbrunton.mymovies.ui.discover

import com.jbrunton.mymovies.ui.discover.genres.GenresViewState
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState

data class DiscoverViewState(
        val nowPlayingViewState: List<MovieSearchResultViewState>,
        val popularViewState: List<MovieSearchResultViewState>,
        val genres: GenresViewState
) {
    companion object {
        val Empty = DiscoverViewState(emptyList(), emptyList(), emptyList())
    }
}