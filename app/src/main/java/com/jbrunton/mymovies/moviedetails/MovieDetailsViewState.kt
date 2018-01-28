package com.jbrunton.mymovies.moviedetails

import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.LoadingViewState

data class MovieDetailsViewState(
        val loadingViewState: LoadingViewState,
        val movie: MovieViewState
)
