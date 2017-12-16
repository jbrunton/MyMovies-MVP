package com.jbrunton.entities;

import java.util.List;

import io.reactivex.Observable;

public interface MoviesRepository {
    Observable<Movie> getMovie(String movieId);
    Observable<List<Movie>> searchMovies(String query);
    Observable<List<Movie>> nowPlaying();
    Observable<List<Movie>> discoverByGenre(String genreId);
}
