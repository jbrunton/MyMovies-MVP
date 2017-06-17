package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {
    @GET("movie/76341")
    Observable<MovieResource> getMovie();

    @GET("search/movie")
    Observable<MoviesCollection> search(@Query("query") String query);
}
