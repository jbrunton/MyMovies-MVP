package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("search/movie")
    Observable<Result<MoviesCollection>> search(@Query("query") String query);

    @GET("movie/now_playing")
    Observable<Result<MoviesCollection>> nowPlaying();

    @GET("genre/movie/list")
    Observable<Result<GenresResponse>> genres();

    @GET("discover/movie")
    Observable<Result<MoviesCollection>> discoverByGenre(@Query("with_genres") String genreId);
}
