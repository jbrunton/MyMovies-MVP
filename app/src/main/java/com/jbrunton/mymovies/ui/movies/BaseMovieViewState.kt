package com.jbrunton.mymovies.ui.movies

import com.jbrunton.entities.models.Movie
import org.joda.time.LocalDate

abstract class BaseMovieViewState(movie: Movie) {
    val movieId = movie.id
    val title = movie.title
    val yearReleased = convertReleaseDate(movie.releaseDate)
    val posterUrl = movie.posterUrl ?: ""
    val backdropUrl = movie.backdropUrl ?: ""
    val rating = "&#9734; ${movie.rating}"

    private fun convertReleaseDate(date: LocalDate?): String {
        return date?.let { Integer.toString(date.year) } ?: ""
    }
}