package com.jbrunton.mymovies.search

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.Failure
import com.jbrunton.mymovies.shared.Loading
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.Success

class SearchViewStateFactory {
    //private val loadingViewStateFactory = LoadingViewStateFactory()

//    private val errorNoResults = LegacyLoadingViewState.errorBuilder()
//            .setErrorMessage("No Results")
//            .setErrorIcon(R.drawable.ic_search_black_24dp)
//            .build()

    private val errorNoResults = Failure<SearchViewState>(
            errorMessage = "No Results",
            errorIcon = R.drawable.ic_search_black_24dp
    )

    private val errorEmptyState = Failure<SearchViewState>(
            errorMessage = "Search",
            errorIcon = R.drawable.ic_search_black_24dp
    )

    val searchEmptyState = errorEmptyState

    val loadingState = Loading<SearchViewState>()

    fun fromList(movies: List<Movie>): LoadingViewState<SearchViewState> {
        return if (movies.isEmpty()) {
            errorEmptyState
        } else {
            Success(movies.map { toMovieSearchResultViewState(it) })
        }
    }

//    fun fromError(throwable: Throwable) =
//            fromLoadingViewState(loadingViewStateFactory.fromError(throwable))
//
//    private fun fromLoadingViewState(loadingViewState: LegacyLoadingViewState): SearchViewState {
//        return SearchViewState(loadingViewState, emptyList())
//    }

    private fun toMovieSearchResultViewState(movie: Movie): MovieSearchResultViewState {
        return MovieSearchResultViewState.Builder()
                .from(movie)
                .build()
    }
}
