package com.jbrunton.mymovies.networking.resources.movies

import com.jbrunton.mymovies.entities.models.Configuration
import com.jbrunton.mymovies.entities.models.Movie
import org.joda.time.LocalDate

class MovieSearchResultResource(
        override val id: String,
        override val originalTitle: String,
        override val posterPath: String,
        override val backdropPath: String,
        override val releaseDate: LocalDate?,
        override val voteAverage: String) : BaseMovieResource {

    fun toMovie(config: Configuration): Movie {
        return Movie(
                id = id,
                title = originalTitle,
                posterUrl = config.expandUrl(posterPath),
                backdropUrl = config.expandUrl(backdropPath),
                releaseDate = releaseDate,
                rating = voteAverage,
                overview = null
        )
    }
}
