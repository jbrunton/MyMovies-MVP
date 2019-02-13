package com.jbrunton.mymovies.ui.search

import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState

data class SearchViewState(val results: List<MovieSearchResultViewState>) {
    companion object {
        val Empty = SearchViewState(emptyList())

        fun from(movies: List<Movie>): SearchViewState {
            return SearchViewState(movies.map(::MovieSearchResultViewState))
        }

//        fun from(state: SearchState): SearchViewState {
//            return when (state) {
//                is SearchState.EmptyQuery -> AsyncResult.failure(SearchViewStateFactory.EmptyStateError)
//                is SearchState.NoResults -> AsyncResult.failure(SearchViewStateFactory.NoResultsError)
//                is SearchState.Some -> AsyncResult.success(from(state.movies))
//            }
//        }
    }
}
