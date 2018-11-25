package com.jbrunton.entities.models

import com.google.common.base.Optional
import org.joda.time.LocalDate

data class Movie(
    val id: String,
    val title: String,
    val posterUrl: Optional<String>,
    val backdropUrl: Optional<String>,
    val releaseDate: Optional<LocalDate>,
    val rating: String,
    val overview: Optional<String>) {
    companion object {
        val emptyMovie = Movie(
                id = "",
                title = "",
                posterUrl = Optional.absent(),
                backdropUrl = Optional.absent(),
                releaseDate = Optional.absent(),
                rating = "",
                overview = Optional.absent()
        )
    }
}
