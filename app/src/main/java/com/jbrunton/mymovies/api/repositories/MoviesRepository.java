package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.app.models.Movie;
import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.resources.MovieResource;
import com.jbrunton.mymovies.api.resources.MoviesCollection;
import com.jbrunton.mymovies.api.services.MovieService;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<MaybeError<Movie>> getMovie(String movieId) {
        return service.movie(movieId)
                .map(this::transformMovieResponse);
    }

    public Observable<MaybeError<List<Movie>>> searchMovies(String query) {
        return service.search(query)
                .map(this::transformMoviesResponse);
    }

    public Observable<MaybeError<List<Movie>>> nowPlaying() {
        return service.nowPlaying()
                .map(this::transformMoviesResponse);
    }

    public Observable<MaybeError<List<Movie>>> discoverByGenre(String genreId) {
        return service.discoverByGenre(genreId)
                .map(this::transformMoviesResponse);
    }

    private MaybeError<Movie> transformMovieResponse(Result<MovieResource> result) {
        if (result.isError()) {
            Throwable throwable = result.error();
            if (throwable instanceof IOException) {
                return MaybeError.fromErrorMessage("There was a problem with your connection.", true);
            } else {
                return MaybeError.fromErrorMessage("There was an unknown error.", false);
            }
        } else {
            Response<MovieResource> response = result.response();
            Movie movie = response.body().toMovie();
            return MaybeError.fromValue(movie);
        }
    }

    private MaybeError<List<Movie>> transformMoviesResponse(Result<MoviesCollection> result) {
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
