package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Movie
import io.reactivex.Observable

interface MoviesRepository {
    fun getMovie(movieId: String): DataStream<Movie>
    fun searchMovies(query: String): DataStream<List<Movie>>
    fun nowPlaying(): DataStream<List<Movie>>
    fun popular(): DataStream<List<Movie>>
    fun discoverByGenre(genreId: String): DataStream<List<Movie>>
    fun favorites(): DataStream<List<Movie>>
    fun favorite(movieId: String): Observable<Unit>
    fun unfavorite(movieId: String): Observable<Unit>
}
