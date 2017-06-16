package com.jbrunton.mymovies;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<Movie> getMovie() {
        return service.getMovie()
                .map(MovieResource::toMovie);
    }
}
