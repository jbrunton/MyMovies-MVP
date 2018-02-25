package com.jbrunton.mymovies.movies

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
