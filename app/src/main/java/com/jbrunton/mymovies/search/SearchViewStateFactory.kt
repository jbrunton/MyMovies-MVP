package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.LoadingViewState

class SearchViewStateFactory {
    companion object {
        val errorNoResults = LoadingViewState.Failure<SearchViewState>(
                errorMessage = "No Results",
                errorIcon = R.drawable.ic_search_black_24dp
        )

        val emptyState = LoadingViewState.Failure<SearchViewState>(
                errorMessage = "Search",
                errorIcon = R.drawable.ic_search_black_24dp
        )

        fun fromList(movies: List<Movie>): LoadingViewState<SearchViewState> {
            if (movies.isEmpty()) {
                return errorNoResults
            } else {
                return LoadingViewState.Success(movies.map { MovieSearchResultViewState(it) })
            }
        }
    }
}