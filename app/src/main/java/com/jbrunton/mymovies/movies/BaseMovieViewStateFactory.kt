package com.jbrunton.mymovies.movies

import com.google.common.base.Optional
import com.jbrunton.entities.Movie
import com.jbrunton.mymovies.shared.LoadingViewStateFactory
import org.joda.time.LocalDate

open class BaseMovieViewStateFactory {
    protected val loadingViewStateFactory = LoadingViewStateFactory()

    protected fun <T : BaseMovieViewState.Builder> setDefaults(builder: T, movie: Movie): T {
        builder.movieId = movie.id
        builder.title = movie.title
        builder.yearReleased = convertReleaseDate(movie.releaseDate)
        builder.rating = "&#9734; " + movie.rating
        builder.posterUrl = emptyIfNull(movie.posterUrl)
        builder.backdropUrl = emptyIfNull(movie.backdropUrl)
        return builder
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

}
