package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.LoadingViewState

typealias MovieDetailsViewState = LoadingViewState<MovieViewState>

fun Movie.toViewState(): MovieViewState {
    val overview = this.overview.get()
    return MovieViewState.Builder().from(this).apply {
        this.overview = overview
    }.build()
}