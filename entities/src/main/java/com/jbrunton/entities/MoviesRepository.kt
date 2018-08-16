package com.jbrunton.entities

import io.reactivex.Observable

interface MoviesRepository {
    fun getMovieLegacy(movieId: String): Observable<Movie>
    suspend fun getMovie(movieId: String): Movie
    fun searchMovies(query: String): Observable<List<Movie>>
    fun nowPlaying(): Observable<List<Movie>>
    fun discoverByGenre(genreId: String): Observable<List<Movie>>
}
