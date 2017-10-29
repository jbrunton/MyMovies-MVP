package com.jbrunton.mymovies.app.movies;

import com.jbrunton.mymovies.models.Movie;

import org.joda.time.LocalDate;

import java.util.Optional;

public class BaseMovieViewStateFactory {
    protected String posterUrl(Movie movie) {
        if (movie.posterPath().isPresent()) {
            return "http://image.tmdb.org/t/p/w300" + movie.posterPath().get();
        } else {
            return "";
        }
    }

    protected String convertReleaseDate(Optional<LocalDate> date) {
        if (date.isPresent()) {
            return Integer.toString(date.get().getYear());
        } else {
            return "";
        }
    }
}
