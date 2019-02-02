package com.jbrunton.entities.repositories

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import io.reactivex.Observable

interface MoviesRepository {
    suspend fun getMovie(movieId: String): AsyncResult<Movie>
    fun getMovieRx(movieId: String): DataStream<Movie>
    fun searchMovies(query: String): DataStream<List<Movie>>
    fun nowPlaying(): DataStream<List<Movie>>
    fun discoverByGenre(genreId: String): DataStream<List<Movie>>
    fun favorites(): DataStream<List<Movie>>
    fun favorite(movieId: String): Observable<Any>
    fun unfavorite(movieId: String): Observable<Any>
}
