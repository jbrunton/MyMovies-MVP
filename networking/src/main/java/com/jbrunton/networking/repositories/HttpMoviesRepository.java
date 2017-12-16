package com.jbrunton.networking.repositories;

import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.networking.resources.configuration.ConfigurationResponse;
import com.jbrunton.networking.resources.movies.MovieDetailsResponse;
import com.jbrunton.networking.resources.movies.MoviesCollection;
import com.jbrunton.networking.services.MovieService;
import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

import java.util.List;

import io.reactivex.Observable;

public class HttpMoviesRepository implements MoviesRepository {
    private final MovieService service;

    public HttpMoviesRepository(MovieService service) {
        this.service = service;
    }

    @Override public Observable<Movie> getMovie(String movieId) {
        return service.movie(movieId)
                .zipWith(config(), MovieDetailsResponse::toMovie);
    }

    @Override public Observable<List<Movie>> searchMovies(String query) {
        return service.search(query)
                .zipWith(config(), MoviesCollection::toCollection);
    }

    @Override public Observable<List<Movie>> nowPlaying() {
        return service.nowPlaying()
                .zipWith(config(), MoviesCollection::toCollection);
    }

    @Override public Observable<List<Movie>> discoverByGenre(String genreId) {
        return service.discoverByGenre(genreId)
                .zipWith(config(), MoviesCollection::toCollection);
    }

    private Observable<Configuration> config() {
        return service.configuration()
                .map(ConfigurationResponse::toModel);
    }
}
