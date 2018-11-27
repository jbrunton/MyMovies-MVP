package com.jbrunton.mymovies.ui.movies

import com.jbrunton.entities.models.Movie

data class MovieViewState(
        val movie: Movie
) : BaseMovieViewState(movie) {
    val overview = movie.overview.or("")
}
