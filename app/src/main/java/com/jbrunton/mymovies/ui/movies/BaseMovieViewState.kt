package com.jbrunton.mymovies.ui.movies

import com.google.common.base.Optional
import com.jbrunton.entities.models.Movie
import org.joda.time.LocalDate

abstract class BaseMovieViewState(movie: Movie) {
    val movieId = movie.id
    val title = movie.title
    val yearReleased = convertReleaseDate(movie.releaseDate)
    val posterUrl = movie.posterUrl.or("")
    val backdropUrl = movie.backdropUrl.or("")
    val rating = "&#9734; ${movie.rating}"

    private fun convertReleaseDate(date: Optional<LocalDate>): String {
        return if (date.isPresent) {
            Integer.toString(date.get().year)
        } else {
            ""
        }
    }
}