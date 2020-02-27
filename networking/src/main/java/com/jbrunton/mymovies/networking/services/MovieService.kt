package com.jbrunton.mymovies.networking.services

import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.models.AuthToken
import com.jbrunton.mymovies.networking.resources.account.AccountResponse
import com.jbrunton.mymovies.networking.resources.account.FavoriteRequest
import com.jbrunton.mymovies.networking.resources.auth.AuthSessionRequest
import com.jbrunton.mymovies.networking.resources.auth.LoginRequest
import com.jbrunton.mymovies.networking.resources.configuration.ConfigurationResponse
import com.jbrunton.mymovies.networking.resources.genres.GenresResponse
import com.jbrunton.mymovies.networking.resources.movies.MovieDetailsResponse
import com.jbrunton.mymovies.networking.resources.movies.MoviesCollection
import io.reactivex.Observable
import retrofit2.http.*

interface MovieService {
    @GET("authentication/token/new")
    suspend fun newAuthToken(): AuthToken

    @POST("authentication/token/validate_with_login")
    suspend fun login(@Body request: LoginRequest): AuthToken

    @POST("authentication/session/new")
    suspend fun newSession(@Body request: AuthSessionRequest): AuthSession

    @GET("configuration")
    fun rxConfiguration(): Observable<ConfigurationResponse>

    @GET("configuration")
    suspend fun configuration(): ConfigurationResponse

    @GET("account")
    suspend fun account(@Query("session_id") sessionId: String): AccountResponse

    @GET("movie/{movie_id}")
    suspend fun movie(@Path("movie_id") movieId: String): MovieDetailsResponse

    @GET("search/movie")
    fun search(@Query("query") query: String): Observable<MoviesCollection>

    @GET("movie/now_playing")
    suspend fun nowPlaying(): MoviesCollection

    @GET("movie/popular")
    suspend fun popular(): MoviesCollection

    @GET("genre/movie/list")
    suspend fun genres(): GenresResponse

    @GET("discover/movie")
    fun discoverByGenre(@Query("with_genres") genreId: String): Observable<MoviesCollection>

    @GET("account/{account_id}/favorite/movies")
    suspend fun favorites(
            @Path("account_id") accountId: String,
            @Query("session_id") sessionId: String
    ): MoviesCollection

    @POST("account/{account_id}/favorite")
    suspend fun favorite(
            @Path("account_id") accountId: String,
            @Query("session_id") sessionId: String,
            @Body request: FavoriteRequest
    )
}
