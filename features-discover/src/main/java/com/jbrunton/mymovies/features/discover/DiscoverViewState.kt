package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.mymovies.shared.ui.movies.list.MovieSearchResultViewState

data class DiscoverViewState(
        val nowPlayingViewState: List<MovieSearchResultViewState>,
        val popularViewState: List<MovieSearchResultViewState>,
        val genres: GenresViewState,
        val genreResults: List<MovieSearchResultViewState>,
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