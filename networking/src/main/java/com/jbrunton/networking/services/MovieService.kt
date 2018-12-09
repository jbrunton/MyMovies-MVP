package com.jbrunton.networking.services

import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.models.AuthToken
import com.jbrunton.networking.resources.account.AccountResponse
import com.jbrunton.networking.resources.auth.AuthSessionRequest
import com.jbrunton.networking.resources.auth.LoginRequest
import com.jbrunton.networking.resources.configuration.ConfigurationResponse
import com.jbrunton.networking.resources.genres.GenresResponse
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.networking.resources.movies.MoviesCollection
import io.reactivex.Observable
import retrofit2.http.*

interface MovieService {
    @GET("authentication/token/new")
    fun newAuthToken(): Observable<AuthToken>

    @POST("authentication/token/validate_with_login")
    fun login(@Body request: LoginRequest): Observable<AuthToken>

    @POST("authentication/session/new")
    fun newSession(@Body request: AuthSessionRequest): Observable<AuthSession>

    @GET("configuration")
    fun configuration(): Observable<ConfigurationResponse>

    @GET("account")
    fun account(@Query("session_id") sessionId: String): Observable<AccountResponse>

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

    @GET("account/{account_id}/favorite/movies")
    fun favorites(@Path("account_id") accountId: String): Observable<MoviesCollection>
}
