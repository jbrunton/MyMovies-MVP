package com.jbrunton.mymovies.ui.discover

import android.view.View
import com.jbrunton.mymovies.ui.discover.genres.GenresViewState
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState

data class DiscoverViewState(
        val nowPlayingViewState: List<MovieSearchResultViewState>,
        val popularViewState: List<MovieSearchResultViewState>,
        val genres: GenresViewState,
        val genreResultsVisibility: Int,
        val genreResults: List<MovieSearchResultViewState>
) {
    companion object {
        val Empty = DiscoverViewState(emptyList(), emptyList(), emptyList(), View.GONE, emptyList())
    }
}