package com.jbrunton.mymovies.networking.resources.movies

import com.jbrunton.mymovies.entities.models.Configuration
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.networking.resources.PagedCollection

class MoviesCollection(
        override val page: Int,
        override val total_results: Int,
        override val total_pages: Int,
        override val results: List<MovieSearchResultResource>
) : PagedCollection<MovieSearchResultResource>() {
    companion object {
        fun toCollection(response: MoviesCollection, config: Configuration): List<Movie> {
            return response.toCollection { resource -> resource.toMovie(config) }
        }
    }
}
