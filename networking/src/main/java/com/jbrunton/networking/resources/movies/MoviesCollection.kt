package com.jbrunton.networking.resources.movies

import com.jbrunton.entities.Configuration
import com.jbrunton.entities.Movie
import com.jbrunton.networking.resources.PagedCollection

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
