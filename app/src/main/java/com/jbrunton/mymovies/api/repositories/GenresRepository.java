package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.entities.Genre;

import java.util.List;

import io.reactivex.Observable;

public class GenresRepository extends BaseRepository {
    private final MovieService service;

    public GenresRepository(MovieService service) {
        this.service = service;
    }

    public Observable<List<Genre>> genres() {
        return service.genres()
                .map(GenresResponse::toCollection);
    }
}
