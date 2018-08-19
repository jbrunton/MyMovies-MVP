package com.jbrunton.networking.services

import com.jbrunton.networking.resources.configuration.ConfigurationResponse
import com.jbrunton.networking.resources.genres.GenresResponse
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RxMovieService {
    @GET("configuration")
    fun configuration(): Observable<ConfigurationResponse>

    @GET("movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: String): Observable<MovieDetailsResponse>

    @GET("search/movie")
    fun search(@Query("query") query: String): Observable<MoviesCollection>

    @GET("movie/now_playing")
    fun nowPlaying(): Observable<MoviesCollection>

    @GET("genre/movie/list")
    fun genres(): Observable<GenresResponse>

    @GET("discover/movie")
    fun discoverByGenre(@Query("with_genres") genreId: String): Observable<MoviesCollection>
}
