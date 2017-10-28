package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.models.Genre;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;

public class GenresRepository extends BaseRepository {
    private final MovieService service;

    public GenresRepository(MovieService service) {
        this.service = service;
    }

    public Observable<MaybeError<List<Genre>>> genres() {
        return service.genres()
                .map(this::transformGenres);
    }

    private MaybeError<List<Genre>> transformGenres(Result<GenresResponse> result) {
        return fromResult(result, GenresResponse::toCollection);
    }
}
