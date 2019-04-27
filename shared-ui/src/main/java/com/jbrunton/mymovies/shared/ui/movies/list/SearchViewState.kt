package com.jbrunton.mymovies.shared.ui.movies.list

import com.jbrunton.mymovies.entities.models.Movie

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        val Empty = SearchViewState(emptyList())

        fun from(movies: List<Movie>): SearchViewState {
            return SearchViewState(movies.map(::MovieSearchResultViewState))
        }
    }
}
