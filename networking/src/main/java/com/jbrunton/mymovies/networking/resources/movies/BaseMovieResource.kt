package com.jbrunton.mymovies.networking.resources.movies

import org.joda.time.LocalDate

interface BaseMovieResource {
    val id: String
    val originalTitle: String
    val posterPath: String
    val backdropPath: String
    val releaseDate: LocalDate?
    val voteAverage: String
}
