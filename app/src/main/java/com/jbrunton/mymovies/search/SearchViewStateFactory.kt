package com.jbrunton.mymovies.search

import com.jbrunton.entities.models.Result
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.shared.LoadingViewStateError

class SearchViewStateFactory {
    companion object {
        val errorNoResults = buildEmptyState("No Results")
        val emptyState = buildEmptyState("Search")

        fun toViewState(movies: List<Movie>): List<MovieSearchResultViewState> {
            return movies.map { MovieSearchResultViewState(it) }
        }

        fun errorIfEmpty(movies: SearchViewState): Result<SearchViewState> {
            return if (movies.isEmpty()) {
                SearchViewStateFactory.errorNoResults
            } else {
                Result.Success(movies)
            }
        }

        private fun buildEmptyState(errorMessage: String): Result.Failure<SearchViewState> {
            return Result.Failure(LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false))
        }
    }
}