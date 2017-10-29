package com.jbrunton.mymovies.app.movies;

import com.jbrunton.mymovies.app.shared.LoadingViewStateFactory;
import com.jbrunton.mymovies.models.Movie;

import org.joda.time.LocalDate;

import java.util.Optional;

public class BaseMovieViewStateFactory {
    protected final LoadingViewStateFactory loadingViewStateFactory = new LoadingViewStateFactory();

    private String posterUrl(Movie movie) {
        if (movie.posterPath().isPresent()) {
            return "http://image.tmdb.org/t/p/w300" + movie.posterPath().get();
        } else {
            return "";
        }
    }

    private String backdropUrl(Movie movie) {
        if (movie.backdropPath().isPresent()) {
            return "http://image.tmdb.org/t/p/w300" + movie.backdropPath().get();
        } else {
            return "";
        }
    }

    private String convertReleaseDate(Optional<LocalDate> date) {
        if (date.isPresent()) {
            return Integer.toString(date.get().getYear());
        } else {
            return "";
        }
    }

    protected <T extends BaseMovieViewState.Builder<T>> T setDefaults(T builder, Movie movie) {
        return builder.movieId(movie.id())
                .title(movie.title())
                .yearReleased(convertReleaseDate(movie.releaseDate()))
                .rating("&#9734; " + movie.rating())
                .posterUrl(posterUrl(movie))
                .backdropUrl(backdropUrl(movie));
    }
}
