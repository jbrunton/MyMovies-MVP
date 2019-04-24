package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie

data class DiscoverResult(
        val nowPlaying: List<Movie>,
        val popular: List<Movie>,
        val genres: List<Genre>,
        val selectedGenre: Genre?,
        val genreResults: List<Movie>
) {
    constructor(nowPlaying: List<Movie>,
                popular: List<Movie>,
                genres: List<Genre>
    ) : this(nowPlaying, popular, genres, null, emptyList())
}
