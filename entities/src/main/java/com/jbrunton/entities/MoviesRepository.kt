package com.jbrunton.entities

import io.reactivex.Observable

interface MoviesRepository {
    fun getMovie(movieId: String): Observable<Movie>
    fun searchMovies(query: String): Observable<List<Movie>>
    fun nowPlaying(): Observable<List<Movie>>
    fun discoverByGenre(genreId: String): Observable<List<Movie>>
}
