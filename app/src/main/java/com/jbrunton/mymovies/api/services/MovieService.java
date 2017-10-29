package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.resources.ConfigurationResponse;
import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.resources.MovieDetailsResponse;
import com.jbrunton.mymovies.api.resources.MoviesCollection;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {
    @GET("configuration")
    Observable<ConfigurationResponse> configuration();

    @GET("movie/{movie_id}")
    Observable<MovieDetailsResponse> movie(@Path("movie_id") String movieId);

    @GET("search/movie")
    Observable<MoviesCollection> search(@Query("query") String query);

    @GET("movie/now_playing")
    Observable<MoviesCollection> nowPlaying();

    @GET("genre/movie/list")
    Observable<GenresResponse> genres();

    @GET("discover/movie")
    Observable<MoviesCollection> discoverByGenre(@Query("with_genres") String genreId);
}
