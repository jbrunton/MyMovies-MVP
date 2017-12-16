package com.jbrunton.mymovies.movies;

import com.google.common.base.Optional;
import com.jbrunton.entities.Movie;
import com.jbrunton.mymovies.shared.LoadingViewStateFactory;

import org.joda.time.LocalDate;

public class BaseMovieViewStateFactory {
    protected final LoadingViewStateFactory loadingViewStateFactory = new LoadingViewStateFactory();

    private String emptyIfNull(Optional<String> s) {
        if (s.isPresent()) {
            return s.get();
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
                .posterUrl(emptyIfNull(movie.posterUrl()))
                .backdropUrl(emptyIfNull(movie.backdropUrl()));
    }
}
