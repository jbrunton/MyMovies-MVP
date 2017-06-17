package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;
import com.jbrunton.mymovies.api.services.MovieService;

import java.util.List;

import io.reactivex.Observable;

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<Movie> getMovie() {
        return service.getMovie()
                .map(MovieResource::toMovie);
    }

    public Observable<List<Movie>> searchMovies(String query) {
        return service.search(query)
                .map(MoviesCollection::toCollection);
    }
}
