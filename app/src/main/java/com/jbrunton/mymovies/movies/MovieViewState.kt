package com.jbrunton.mymovies.movies

data class MovieViewState(
        override val movieId: String,
        override val title: String,
        override val yearReleased: String,
        override val posterUrl: String,
        override val backdropUrl: String,
        override val rating: String,
        val overview: String
) : BaseMovieViewState {

    companion object {
        val EMPTY = Builder().build()
    }

    class Builder() : BaseMovieViewState.Builder() {
        var overview: String = ""

        fun build() = MovieViewState(
                movieId = movieId,
                title = title,
                yearReleased = yearReleased,
                posterUrl = posterUrl,
                backdropUrl = backdropUrl,
                rating = rating,
                overview = overview
        )
    }
}
