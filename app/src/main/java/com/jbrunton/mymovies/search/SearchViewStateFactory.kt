package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.LoadingViewStateFactory

class SearchViewStateFactory {
    private val loadingViewStateFactory = LoadingViewStateFactory()

    fun fromList(movies: List<Movie>): SearchViewState {
        return if (movies.isEmpty()) {
            fromLoadingViewState(LoadingViewState.errorBuilder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build())
        } else {
            SearchViewState.builder()
                    .setLoadingViewState(LoadingViewState.OK_STATE)
                    .setMovies(movies.map { this.toMovieSearchResultViewState(it) })
                    .build()
        }
    }

    fun fromError(throwable: Throwable): SearchViewState {
        return fromLoadingViewState(loadingViewStateFactory.fromError(throwable))
    }

    fun searchEmptyState(): SearchViewState {
        return fromLoadingViewState(
                LoadingViewState.errorBuilder()
                        .setErrorMessage("Search")
                        .setErrorIcon(R.drawable.ic_search_black_24dp)
                        .build())
    }

    fun loadingState(): SearchViewState {
        return fromLoadingViewState(LoadingViewState.LOADING_STATE)
    }

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
