package com.jbrunton.mymovies.movies

import com.jbrunton.entities.Movie

data class MovieSearchResultViewState(
        override val movieId: String,
        override val title: String,
        override val yearReleased: String,
        override val posterUrl: String,
        override val backdropUrl: String,
        override val rating: String
) : BaseMovieViewState {

    class Builder : BaseMovieViewState.Builder() {
        fun build() = MovieSearchResultViewState(
                movieId = movieId,
                title = title,
                yearReleased = yearReleased,
                posterUrl = posterUrl,
                backdropUrl = backdropUrl,
                rating = rating
        )
    }
}

fun Movie.toSearchResultViewState(): MovieSearchResultViewState {
    return MovieSearchResultViewState.Builder()
            .from(this)
            .build()
}