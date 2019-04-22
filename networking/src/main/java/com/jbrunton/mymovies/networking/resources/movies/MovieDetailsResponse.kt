package com.jbrunton.mymovies.networking.resources.movies

import com.jbrunton.mymovies.entities.models.Configuration
import com.jbrunton.mymovies.entities.models.Movie
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
                    releaseDate = response.releaseDate,
                    rating = response.voteAverage,
                    overview = response.overview
            )
        }
    }
}
