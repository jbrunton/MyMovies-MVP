package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.mymovies.entities.models.Movie

interface MoviesRepository {
    suspend fun getMovie(movieId: String): FlowDataStream<Movie>
    suspend fun searchMovies(query: String): FlowDataStream<List<Movie>>
    suspend fun nowPlaying(): FlowDataStream<List<Movie>>
    suspend fun popular(): FlowDataStream<List<Movie>>
    suspend fun discoverByGenre(genreId: String): FlowDataStream<List<Movie>>
    suspend fun favorites(): FlowDataStream<List<Movie>>
    suspend fun favorite(movieId: String): FlowDataStream<Unit>
    suspend fun unfavorite(movieId: String): FlowDataStream<Unit>
}
