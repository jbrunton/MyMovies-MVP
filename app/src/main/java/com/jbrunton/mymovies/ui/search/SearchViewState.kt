package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onSuccess
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewStateError
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        val Empty = SearchViewState(emptyList())

        fun from(movies: List<Movie>): SearchViewState {
            return SearchViewState(movies.map(::MovieSearchResultViewState))
        }
    }

    class Builder(val result: AsyncResult<List<Movie>>) {
        companion object {
            val NoResults = buildEmptyState("No Results")
            val EmptyState = buildEmptyState("Search")
                    .toLoadingViewState(SearchViewState.Empty)

            private fun buildEmptyState(errorMessage: String): AsyncResult.Failure<SearchViewState> {
                return AsyncResult.Failure(LoadingViewStateError(errorMessage, R.drawable.ic_search_black_24dp, false))
            }
        }

        fun asResult(): AsyncResult<SearchViewState> {
            return result
                    .map(SearchViewState.Companion::from)
                    .handleNetworkErrors()
                    .onSuccess {
                        errorIfEmpty(it.value)
                    }
        }

        fun asLoadingViewState(): LoadingViewState<SearchViewState> {
            return asResult().toLoadingViewState(Empty)
        }

        private fun errorIfEmpty(movies: SearchViewState): AsyncResult<SearchViewState> {
            return if (movies.results.isEmpty()) {
                NoResults
            } else {
                AsyncResult.Success(movies)
            }
        }

    }
}
