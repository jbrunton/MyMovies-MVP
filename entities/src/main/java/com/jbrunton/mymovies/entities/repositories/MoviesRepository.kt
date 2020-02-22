package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.mymovies.entities.models.Movie

interface MoviesRepository {
    fun getMovie(movieId: String): DataStream<Movie>
    fun searchMovies(query: String): DataStream<List<Movie>>
    fun nowPlaying(): DataStream<List<Movie>>
    fun popular(): DataStream<List<Movie>>
    fun discoverByGenre(genreId: String): DataStream<List<Movie>>
    suspend fun favorites(): FlowDataStream<List<Movie>>
    suspend fun favorite(movieId: String): FlowDataStream<Unit>
    suspend fun unfavorite(movieId: String): FlowDataStream<Unit>
}
