package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;
import com.jbrunton.mymovies.api.services.MovieService;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<MaybeError<List<Movie>>> searchMovies(String query) {
        return service.search(query)
                .map(this::transformResponse);
    }

    public Observable<MaybeError<List<Movie>>> nowPlaying() {
        return service.nowPlaying()
                .map(this::transformResponse);
    }

    private MaybeError<List<Movie>> transformResponse(Result<MoviesCollection> result) {
        if (result.isError()) {
            Throwable throwable = result.error();
            if (throwable instanceof IOException) {
                return MaybeError.fromErrorMessage("There was a problem with your connection.", true);
            } else {
                return MaybeError.fromErrorMessage("There was an unknown error.", false);
            }
        } else {
            Response<MoviesCollection> response = result.response();
            List<Movie> locations = response.body().toCollection();
            return MaybeError.fromValue(locations);
        }
    }
}
