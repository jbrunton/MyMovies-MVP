package com.jbrunton.mymovies.converters;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.models.Movie;
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewState;
import com.jbrunton.mymovies.search.MovieViewState;
import com.jbrunton.mymovies.search.SearchViewState;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieResultsConverter {
    public SearchViewState toSearchViewState(List<Movie> movies) {
        if (movies.isEmpty()) {
            return emptySearchViewState(LoadingViewState.builder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
        } else {
            return SearchViewState.builder()
                    .setLoadingViewState(LoadingViewState.OK_STATE)
                    .setMovies(movies.stream().map(this::toMovieViewState).collect(Collectors.toList()))
                    .build();
        }
    }

    public SearchViewState toSearchViewState(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return  emptySearchViewState(LoadingViewState.builder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build());
    }

    public MovieDetailsViewState toMovieDetailsViewState(Movie movie) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.OK_STATE)
                .setMovie(Optional.of(toMovieViewState(movie)))
                .build();
    }

    public MovieDetailsViewState toMovieDetailsViewState(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.errorBuilder()
                        .setErrorMessage(error.getMessage())
                        .setErrorIcon(resId)
                        .setShowTryAgainButton(true)
                        .build())
                .setMovie(Optional.empty())
                .build();
    }

    public SearchViewState emptySearchViewState(LoadingViewState loadingViewState) {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setLoadingViewState(loadingViewState)
                .build();
    }

    public MovieViewState toMovieViewState(Movie movie) {
        return MovieViewState.builder()
                .setMovieId(movie.id())
                .setTitle(movie.title())
                .setYearReleased(convertReleaseDate(movie.releaseDate()))
                .setRating("&#9734; " + movie.rating())
                .setPosterUrl("http://image.tmdb.org/t/p/w300" + movie.posterPath())
                .setOverview(movie.overview())
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
