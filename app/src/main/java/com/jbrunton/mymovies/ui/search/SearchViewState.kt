package com.jbrunton.mymovies.ui.search

import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        val Empty = SearchViewState(emptyList())
    }
}
