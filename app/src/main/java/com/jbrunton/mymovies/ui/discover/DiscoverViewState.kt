package com.jbrunton.mymovies.ui.discover

import android.view.View
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState

data class DiscoverViewState(
        val nowPlayingViewState: List<com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState>,
        val popularViewState: List<com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState>,
        val genres: GenresViewState,
        val genreResults: List<com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState>,
        val genreResultsVisibility: Int,
        val genreResultsLoadingIndicatorVisibility: Int,
        val scrollToGenreResults: Boolean
) {
    companion object {
        val Empty = DiscoverViewState(
                nowPlayingViewState = emptyList(),
                popularViewState = emptyList(),
                genres = emptyList(),
                genreResults = emptyList(),
                genreResultsVisibility = View.GONE,
                genreResultsLoadingIndicatorVisibility = View.GONE,
                scrollToGenreResults = false)
    }
}