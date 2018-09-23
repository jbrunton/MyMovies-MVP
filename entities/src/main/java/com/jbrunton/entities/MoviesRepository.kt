package com.jbrunton.entities

import io.reactivex.Observable

interface MoviesRepository {
    suspend fun getMovie(movieId: String): Movie
    fun getMovieRx(movieId: String): Observable<Movie>
    fun searchMovies(query: String): Observable<List<Movie>>
    fun nowPlaying(): Observable<List<Movie>>
    fun discoverByGenre(genreId: String): Observable<List<Movie>>
}
