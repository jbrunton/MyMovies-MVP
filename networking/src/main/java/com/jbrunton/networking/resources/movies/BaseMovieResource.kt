package com.jbrunton.networking.resources.movies

import org.joda.time.LocalDate

interface BaseMovieResource {
    val id: String
    val originalTitle: String
    val posterPath: String
    val backdropPath: String
    val releaseDate: LocalDate?
    val voteAverage: String

//    protected fun builder(config: Configuration): Movie.Builder {
//        return Movie.Companion.builder()
//                .id(id)
//                .title(originalTitle)
//                .posterUrl(config.expandUrl(posterPath))
//                .backdropUrl(config.expandUrl(backdropPath))
//                .releaseDate(Optional.fromNullable<T>(releaseDate))
//                .rating(voteAverage)
//    }
}
