package com.jbrunton.networking.repositories;

import com.jbrunton.entities.Genre;
import com.jbrunton.entities.GenresRepository;
import com.jbrunton.networking.resources.genres.GenresResponse;
import com.jbrunton.networking.services.LegacyMovieService;

import java.util.List;

import io.reactivex.Observable;

public class HttpGenresRepository implements GenresRepository {
    private final LegacyMovieService service;

    public HttpGenresRepository(LegacyMovieService service) {
        this.service = service;
    }

    @Override public Observable<List<Genre>> genres() {
        return service.genres()
                .map(GenresResponse::toCollection);
    }
}
