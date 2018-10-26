package com.jbrunton.mymovies.search

import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.LegacyLoadingViewState

data class SearchViewState(
        val loadingViewState: LegacyLoadingViewState,
        val movies: List<MovieSearchResultViewState>)
