package com.jbrunton.mymovies.usecases.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie

data class DiscoverState(
        val nowPlaying: List<Movie>,
        val popular: List<Movie>,
        val genres: List<Genre>
)
