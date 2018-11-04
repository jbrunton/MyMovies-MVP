package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.LoadingViewState

class SearchViewStateFactory {
    companion object {
        val errorNoResults = buildEmptyState("No Results")
        val emptyState = buildEmptyState("Search")

        fun toViewState(movies: List<Movie>): List<MovieSearchResultViewState> {
            return movies.map { MovieSearchResultViewState(it) }
        }

        fun errorIfEmpty(movies: SearchViewState): LoadingViewState<SearchViewState> {
            return if (movies.isEmpty()) {
                SearchViewStateFactory.errorNoResults
            } else {
                LoadingViewState.Success(movies)
            }
        }

        private fun buildEmptyState(errorMessage: String): LoadingViewState.Failure<SearchViewState> {
            return LoadingViewState.Failure(
                    errorMessage = errorMessage,
                    errorIcon = R.drawable.ic_search_black_24dp
            )
        }
    }
}