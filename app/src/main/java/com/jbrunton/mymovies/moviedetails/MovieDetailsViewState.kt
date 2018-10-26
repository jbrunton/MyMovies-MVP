package com.jbrunton.mymovies.moviedetails

import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.LegacyLoadingViewState

data class MovieDetailsViewState(
        val loadingViewState: LegacyLoadingViewState,
        val movie: MovieViewState
)
