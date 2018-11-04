package com.jbrunton.networking.resources.movies

import com.google.common.base.Optional
import com.jbrunton.entities.models.Configuration
import com.jbrunton.entities.models.Movie
import org.joda.time.LocalDate

data class MovieDetailsResponse(
        override val id: String,
        override val originalTitle: String,
        override val posterPath: String,
        override val backdropPath: String,
        override val releaseDate: LocalDate?,
        override val voteAverage: String,
        val overview: String?) : BaseMovieResource {

    companion object {
        fun toMovie(response: MovieDetailsResponse, config: Configuration): Movie {

            return Movie(
                    id = response.id,
                    title = response.originalTitle,
                    posterUrl = config.expandUrl(response.posterPath),
                    backdropUrl = config.expandUrl(response.backdropPath),
                    releaseDate = Optional.fromNullable(response.releaseDate),
                    rating = response.voteAverage,
                    overview = Optional.fromNullable(response.overview)
            )
        }
    }
}
