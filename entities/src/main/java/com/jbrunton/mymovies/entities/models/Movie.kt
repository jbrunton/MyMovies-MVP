package com.jbrunton.mymovies.entities.models

import org.joda.time.LocalDate

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: String?,
    val backdropUrl: String?,
    val releaseDate: LocalDate?,
    val rating: String,
    val overview: String?) {
    companion object {
        val emptyMovie = Movie(
                id = "",
                title = "",
                posterUrl = null,
                backdropUrl = null,
                releaseDate = null,
                rating = "",
                overview = null
        )
    }
}
