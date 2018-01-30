package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.LoadingViewStateFactory

class SearchViewStateFactory {
    private val loadingViewStateFactory = LoadingViewStateFactory()

    private val errorNoResults = LoadingViewState.errorBuilder()
            .setErrorMessage("No Results")
            .setErrorIcon(R.drawable.ic_search_black_24dp)
            .build()

    private val errorEmptyState = LoadingViewState.errorBuilder()
            .setErrorMessage("Search")
            .setErrorIcon(R.drawable.ic_search_black_24dp)
            .build()

    val searchEmptyState = fromLoadingViewState(errorEmptyState)

    val loadingState = fromLoadingViewState(LoadingViewState.LOADING_STATE)

    fun fromList(movies: List<Movie>): SearchViewState {
        return if (movies.isEmpty()) {
            fromLoadingViewState(errorNoResults)
        } else {
            SearchViewState.builder()
                    .setLoadingViewState(LoadingViewState.OK_STATE)
                    .setMovies(movies.map { this.toMovieSearchResultViewState(it) })
                    .build()
        }
    }

    fun fromError(throwable: Throwable) =
            fromLoadingViewState(loadingViewStateFactory.fromError(throwable))

    private fun fromLoadingViewState(loadingViewState: LoadingViewState): SearchViewState {
        return SearchViewState.builder()
                .setMovies(emptyList())
                .setLoadingViewState(loadingViewState)
                .build()
    }

    private fun toMovieSearchResultViewState(movie: Movie): MovieSearchResultViewState {
        return MovieSearchResultViewState.Builder()
                .from(movie)
                .build()
    }
}
