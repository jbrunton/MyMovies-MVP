package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.movies.BaseMovieViewStateFactory
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.shared.LoadingViewState

class MovieDetailsViewStateFactory : BaseMovieViewStateFactory() {
    fun fromMovie(movie: Movie) = MovieDetailsViewState(
            loadingViewState = LoadingViewState.OK_STATE,
            movie = toMovieViewState(movie))

    fun fromError(throwable: Throwable) = MovieDetailsViewState(
            loadingViewState = loadingViewStateFactory.fromError(throwable),
            movie = MovieViewState.EMPTY)

    fun loadingState() = MovieDetailsViewState(
                loadingViewState = LoadingViewState.LOADING_STATE,
                movie = MovieViewState.EMPTY)

    private fun toMovieViewState(movie: Movie): MovieViewState {
        val builder = setDefaults(MovieViewState.Builder(), movie)
        builder.overview = movie.overview.get()
        return builder.build()
    }
}
