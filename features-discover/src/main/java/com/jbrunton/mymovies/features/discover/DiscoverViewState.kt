package com.jbrunton.mymovies.features.discover

import android.view.View
import com.jbrunton.mymovies.shared.ui.MovieSearchResultViewState

data class GenresViewState(
        val genres: List<GenreChipViewState>,
        val genreResults: List<MovieSearchResultViewState>,
        val genreResultsVisibility: Int,
        val genreResultsLoadingIndicatorVisibility: Int
) {
    companion object {
        val Empty = GenresViewState(
                genres = emptyList(),
                genreResults = emptyList(),
                genreResultsVisibility = View.GONE,
                genreResultsLoadingIndicatorVisibility = View.GONE)
    }
}

data class DiscoverViewState(
        val nowPlayingViewState: List<MovieSearchResultViewState>,
        val popularViewState: List<MovieSearchResultViewState>,
        val genresViewState: GenresViewState,
        val scrollToGenreResults: Boolean
) {
    companion object {
        val Empty = DiscoverViewState(
                nowPlayingViewState = emptyList(),
                popularViewState = emptyList(),
                genresViewState = GenresViewState.Empty,
                scrollToGenreResults = false)
    }
}