package com.jbrunton.mymovies.movies

import com.google.common.base.Optional
import com.jbrunton.entities.Movie
import org.joda.time.LocalDate

interface BaseMovieViewState {
    val movieId: String
    val title: String
    val yearReleased: String
    val posterUrl: String
    val backdropUrl: String
    val rating: String

    open class Builder {
        var movieId: String = ""
        var title: String = ""
        var yearReleased: String = ""
        var posterUrl: String = ""
        var backdropUrl: String = ""
        var rating: String = ""
    }
}

fun <T : BaseMovieViewState.Builder> T.from(movie: Movie): T {
    movieId = movie.id
    title = movie.title
    yearReleased = convertReleaseDate(movie.releaseDate)
    rating = "&#9734; " + movie.rating
    posterUrl = emptyIfNull(movie.posterUrl)
    backdropUrl = emptyIfNull(movie.backdropUrl)
    return this
}

private fun emptyIfNull(s: Optional<String>): String {
    return if (s.isPresent) {
        s.get()
    } else {
        ""
    }
}

private fun convertReleaseDate(date: Optional<LocalDate>): String {
    return if (date.isPresent) {
        Integer.toString(date.get().year)
    } else {
        ""
    }
}
