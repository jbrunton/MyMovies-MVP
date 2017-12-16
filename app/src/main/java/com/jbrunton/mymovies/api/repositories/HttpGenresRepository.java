package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.entities.Genre;
import com.jbrunton.entities.GenresRepository;
import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.services.MovieService;

import java.util.List;

import io.reactivex.Observable;

public class HttpGenresRepository implements GenresRepository {
    private final MovieService service;

    public HttpGenresRepository(MovieService service) {
        this.service = service;
    }

    @Override public Observable<List<Genre>> genres() {
        return service.genres()
                .map(GenresResponse::toCollection);
    }
}
