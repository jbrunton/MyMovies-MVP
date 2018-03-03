package com.jbrunton.mymovies.search

import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.LoadingViewState

data class SearchViewState(
        val loadingViewState: LoadingViewState,
        val movies: List<MovieSearchResultViewState>)