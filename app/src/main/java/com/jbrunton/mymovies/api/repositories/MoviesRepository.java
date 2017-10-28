package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.MaybeError;
import com.jbrunton.mymovies.api.resources.MovieDetailsResponse;
import com.jbrunton.mymovies.api.resources.MoviesCollection;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.models.Movie;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;

public class MoviesRepository extends BaseRepository {
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

    private MaybeError<Movie> transformMovieResponse(Result<MovieDetailsResponse> result) {
        return fromResult(result, MovieDetailsResponse::toMovie);
    }

    private MaybeError<List<Movie>> transformMoviesResponse(Result<MoviesCollection> result) {
        return fromResult(result, MoviesCollection::toCollection);
    }
}
