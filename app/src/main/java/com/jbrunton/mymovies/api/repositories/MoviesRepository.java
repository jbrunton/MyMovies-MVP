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

public class MoviesRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<MaybeError<Movie>> getMovie(String movieId) {
        return service.movie(movieId)
                .map(result -> {
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
                });
    }

    public Observable<MaybeError<List<Movie>>> searchMovies(String query) {
        return service.search(query)
                .map(result -> {
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
                });
    }

    public Observable<MaybeError<List<Movie>>> nowPlaying() {
        return service.nowPlaying()
                .map(result -> {
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
                });
    }

    public Observable<MaybeError<List<Movie>>> discoverByGenre(String genreId) {
        return service.discoverByGenre(genreId)
                .map(result -> {
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
                });
    }

}
