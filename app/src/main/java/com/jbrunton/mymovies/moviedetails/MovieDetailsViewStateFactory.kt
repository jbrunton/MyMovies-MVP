package com.jbrunton.mymovies.moviedetails

import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.movies.MovieViewState
import com.jbrunton.mymovies.movies.from
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.LoadingViewStateFactory

class MovieDetailsViewStateFactory {
    private val loadingViewStateFactory = LoadingViewStateFactory()

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
        return MovieViewState.Builder().from(movie).apply {
            this.overview = movie.overview.get()
        }.build()
    }
}
