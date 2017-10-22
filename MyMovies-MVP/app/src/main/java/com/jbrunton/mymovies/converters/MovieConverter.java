package com.jbrunton.mymovies.converters;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.search.SearchItemViewState;
import com.jbrunton.mymovies.search.SearchViewState;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieConverter {
    public SearchViewState convertMoviesResponse(List<Movie> movies) {
        if (movies.isEmpty()) {
            return errorBuilder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build();
        } else {
            return SearchViewState.builder()
                    .setShowError(false)
                    .setMovies(movies.stream().map(this::convertMovie).collect(Collectors.toList()))
                    .build();
        }
    }

    public SearchViewState convertErrorResponse(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build();
    }

    public SearchViewState.Builder errorBuilder() {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setShowError(true);
    }

    private SearchItemViewState convertMovie(Movie movie) {
        return SearchItemViewState.builder()
                .setTitle(movie.getTitle())
                .setYearReleased(convertReleaseDate(movie.getReleaseDate()))
                .setRating("&#9734; " + movie.getRating())
                .setPosterUrl("http://image.tmdb.org/t/p/w300" + movie.getPosterPath())
                .build();
    }

    private String convertReleaseDate(Optional<LocalDate> date) {
        if (date.isPresent()) {
            return Integer.toString(date.get().getYear());
        } else {
            return "";
        }
    }
}
