package com.jbrunton.mymovies.ui.movies

import android.view.View
import com.jbrunton.entities.models.Movie

data class MovieViewState(
        val movie: Movie,
        val favoriteVisibility: Int,
        val unfavoriteVisibility: Int
) : BaseMovieViewState(movie) {
    val overview = movie.overview.or("")

    companion object {
        fun from(movie: Movie, favorite: Boolean) = MovieViewState(movie,
                if (favorite) { View.GONE } else { View.VISIBLE },
                if (favorite) { View.VISIBLE } else { View.GONE }
        )

        val Empty = MovieViewState(
                Movie.emptyMovie,
                View.GONE,
                View.GONE
        )
    }
}
