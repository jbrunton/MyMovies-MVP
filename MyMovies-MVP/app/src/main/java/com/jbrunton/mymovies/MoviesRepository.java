package com.jbrunton.mymovies;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable getMovie() {
        Function<MovieResource, Movie> toMovie = new Function<MovieResource, Movie>() {
            @Override public Movie apply(@NonNull MovieResource movieResource) throws Exception {
                return movieResource.toMovie();
            }
        };
        return service.getMovie().map(toMovie);
    }
}
