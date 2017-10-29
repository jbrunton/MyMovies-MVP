package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.resources.ConfigurationResponse;
import com.jbrunton.mymovies.api.resources.MovieDetailsResponse;
import com.jbrunton.mymovies.api.resources.MoviesCollection;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.models.Configuration;
import com.jbrunton.mymovies.models.Movie;

import java.util.List;

import io.reactivex.Observable;

public class MoviesRepository extends BaseRepository {
    private final MovieService service;

    public MoviesRepository(MovieService service) {
        this.service = service;
    }

    public Observable<Movie> getMovie(String movieId) {
        return service.movie(movieId)
                .zipWith(config(), MovieDetailsResponse::toMovie);
    }

    public Observable<List<Movie>> searchMovies(String query) {
        return service.search(query)
                .zipWith(config(), MoviesCollection::toCollection);
    }

    public Observable<List<Movie>> nowPlaying() {
        return service.nowPlaying()
                .zipWith(config(), MoviesCollection::toCollection);
    }

    public Observable<List<Movie>> discoverByGenre(String genreId) {
        return service.discoverByGenre(genreId)
                .zipWith(config(), MoviesCollection::toCollection);
    }

    private Observable<Configuration> config() {
        return service.configuration()
                .map(ConfigurationResponse::toModel);
    }


}
