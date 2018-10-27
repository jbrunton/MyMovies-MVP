package com.jbrunton.mymovies.movies

import com.jbrunton.entities.Movie

data class MovieViewState(
        val movie: Movie
) : BaseMovieViewState(movie) {
    val overview = movie.overview.or("")
}
