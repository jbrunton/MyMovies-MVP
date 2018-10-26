package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.Loading
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.LoadingViewStateFactory
import com.jbrunton.mymovies.shared.Success

class MovieDetailsViewStateFactory {
    private val loadingViewStateFactory = LoadingViewStateFactory()

    fun fromMovie(movie: Movie) = Success(toMovieViewState(movie))

    fun fromError(throwable: Throwable): LoadingViewState<MovieViewState> = LoadingViewState.fromError(throwable)

    fun loadingState(): LoadingViewState<MovieViewState> = Loading()

    private fun toMovieViewState(movie: Movie): MovieViewState {
        return MovieViewState.Builder().from(movie).apply {
            this.overview = movie.overview.get()
        }.build()
    }
}
