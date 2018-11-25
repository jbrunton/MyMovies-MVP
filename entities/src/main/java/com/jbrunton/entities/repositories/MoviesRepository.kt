package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Movie

interface MoviesRepository {
    fun getMovie(movieId: String): DataStream<Movie>
    fun searchMovies(query: String): DataStream<List<Movie>>
    fun nowPlaying(): DataStream<List<Movie>>
    fun discoverByGenre(genreId: String): DataStream<List<Movie>>
}
