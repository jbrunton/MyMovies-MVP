package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;

import io.reactivex.Observable;
import retrofit2.http.GET;

public interface MovieService {
    @GET("movie/76341?api_key=5b07463cf2bdde60339c9621dcd6e226")
    Observable<MovieResource> getMovie();

    @GET("search/movie?api_key=5b07463cf2bdde60339c9621dcd6e226&query=Star%20Trek")
    Observable<MoviesCollection> search();
}
