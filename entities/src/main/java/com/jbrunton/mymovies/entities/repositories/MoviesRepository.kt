package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.mymovies.entities.models.Movie

interface MoviesRepository {
    suspend fun getMovie(movieId: String): DataStream<Movie>
    suspend fun searchMovies(query: String): DataStream<List<Movie>>
    suspend fun nowPlaying(): DataStream<List<Movie>>
    suspend fun popular(): DataStream<List<Movie>>
    suspend fun discoverByGenre(genreId: String): DataStream<List<Movie>>
    suspend fun favorites(): DataStream<List<Movie>>
    suspend fun favorite(movieId: String): DataStream<Unit>
    suspend fun unfavorite(movieId: String): DataStream<Unit>
}
