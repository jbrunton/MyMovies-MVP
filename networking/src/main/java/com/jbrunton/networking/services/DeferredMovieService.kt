package com.jbrunton.networking.services

import com.jbrunton.networking.resources.configuration.ConfigurationResponse
import com.jbrunton.networking.resources.movies.MovieDetailsResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path

interface DeferredMovieService {
    @GET("configuration")
    fun configuration(): Deferred<ConfigurationResponse>

    @GET("movie/{movie_id}")
    fun movie(@Path("movie_id") movieId: String): Deferred<MovieDetailsResponse>
}