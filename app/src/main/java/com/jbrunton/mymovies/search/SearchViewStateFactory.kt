package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.LegacyLoadingViewState
import com.jbrunton.mymovies.shared.LoadingViewStateFactory

class SearchViewStateFactory {
    private val loadingViewStateFactory = LoadingViewStateFactory()

    private val errorNoResults = LegacyLoadingViewState.errorBuilder()
            .setErrorMessage("No Results")
            .setErrorIcon(R.drawable.ic_search_black_24dp)
            .build()

    private val errorEmptyState = LegacyLoadingViewState.errorBuilder()
            .setErrorMessage("Search")
            .setErrorIcon(R.drawable.ic_search_black_24dp)
            .build()

    val searchEmptyState = fromLoadingViewState(errorEmptyState)

    val loadingState = fromLoadingViewState(LegacyLoadingViewState.LOADING_STATE)

    fun fromList(movies: List<Movie>): SearchViewState {
        return if (movies.isEmpty()) {
            fromLoadingViewState(errorNoResults)
        } else {
            SearchViewState(
                    LegacyLoadingViewState.OK_STATE,
                    movies.map { toMovieSearchResultViewState(it) })
        }
    }

    fun fromError(throwable: Throwable) =
            fromLoadingViewState(loadingViewStateFactory.fromError(throwable))

    private fun fromLoadingViewState(loadingViewState: LegacyLoadingViewState): SearchViewState {
        return SearchViewState(loadingViewState, emptyList())
    }

    private fun toMovieSearchResultViewState(movie: Movie): MovieSearchResultViewState {
        return MovieSearchResultViewState.Builder()
                .from(movie)
                .build()
    }
}
