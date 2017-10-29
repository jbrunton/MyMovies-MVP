package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.resources.GenresResponse;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.models.Genre;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class GenresRepository {
    private final MovieService service;

    public GenresRepository(MovieService service) {
        this.service = service;
    }

    public Observable<MaybeError<List<Genre>>> genres() {
        return service.genres()
                .map(this::transformResponse);
    }

    private MaybeError<List<Genre>> transformResponse(Result<GenresResponse> result) {
        if (result.isError()) {
            Throwable throwable = result.error();
            if (throwable instanceof IOException) {
                return MaybeError.fromErrorMessage("There was a problem with your connection.", true);
            } else {
                return MaybeError.fromErrorMessage("There was an unknown error.", false);
            }
        } else {
            Response<GenresResponse> response = result.response();
            List<Genre> genres = response.body().toCollection();
            return MaybeError.fromValue(genres);
        }
    }
}
