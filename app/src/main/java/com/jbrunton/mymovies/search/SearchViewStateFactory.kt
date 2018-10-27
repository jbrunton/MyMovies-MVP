package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.Failure
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.Success

class SearchViewStateFactory {
    companion object {
        val errorNoResults = Failure<SearchViewState>(
                errorMessage = "No Results",
                errorIcon = R.drawable.ic_search_black_24dp
        )

        val emptyState = Failure<SearchViewState>(
                errorMessage = "Search",
                errorIcon = R.drawable.ic_search_black_24dp
        )

        fun fromList(movies: List<Movie>): LoadingViewState<SearchViewState> {
            if (movies.isEmpty()) {
                return errorNoResults
            } else {
                return Success(movies.map { MovieSearchResultViewState(it) })
            }
        }
    }
}