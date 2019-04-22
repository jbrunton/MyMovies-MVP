package com.jbrunton.mymovies.ui.moviedetails

import android.view.View
import com.jbrunton.mymovies.entities.models.Movie

data class MovieDetailsViewState(
        val movie: Movie,
        val favoriteVisibility: Int,
        val unfavoriteVisibility: Int
) : com.jbrunton.mymovies.shared.ui.BaseMovieViewState(movie) {
    val overview = movie.overview ?: ""

    companion object {
        fun from(movie: Movie, favorite: Boolean) = MovieDetailsViewState(
                movie = movie,
                favoriteVisibility = if (favorite) { View.GONE } else { View.VISIBLE },
                unfavoriteVisibility = if (favorite) { View.VISIBLE } else { View.GONE }
        )

        val Empty = MovieDetailsViewState(
                Movie.emptyMovie,
                View.GONE,
                View.GONE
        )
    }
}
