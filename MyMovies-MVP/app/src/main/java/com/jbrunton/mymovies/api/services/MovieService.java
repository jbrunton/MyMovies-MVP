package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MovieService {
    @GET("movie/76341")
    Observable<MovieResource> getMovie();

    @GET("search/movie?query=Star%20Trek")
    Observable<MoviesCollection> search();
}
