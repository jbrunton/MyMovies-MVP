package com.jbrunton.mymovies.app.converters;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.models.Movie;
import com.jbrunton.mymovies.app.moviedetails.MovieDetailsViewState;
import com.jbrunton.mymovies.app.movies.MovieSearchResultViewState;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.search.SearchViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MovieResultsConverter {
    public SearchViewState toSearchViewState(List<Movie> movies) {
        if (movies.isEmpty()) {
            return emptySearchViewState(LoadingViewState.errorBuilder()
                    .setErrorMessage("No Results")
                    .setErrorIcon(R.drawable.ic_search_black_24dp)
                    .build());
        } else {
            return SearchViewState.builder()
                    .setLoadingViewState(LoadingViewState.OK_STATE)
                    .setMovies(movies.stream().map(this::toMovieSearchResultViewState).collect(Collectors.toList()))
                    .build();
        }
    }

    public SearchViewState toSearchViewState(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return  emptySearchViewState(LoadingViewState.errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build());
    }

    public MovieDetailsViewState toMovieDetailsViewState(Movie movie) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.OK_STATE)
                .setMovie(toMovieViewState(movie))
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
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    public SearchViewState emptySearchViewState(LoadingViewState loadingViewState) {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setLoadingViewState(loadingViewState)
                .build();
    }

    public MovieSearchResultViewState toMovieSearchResultViewState(Movie movie) {
        Optional<LocalDate> date = movie.releaseDate();
        String result;
        if (date.isPresent()) {
            result = Integer.toString(date.get().getYear());
        } else {
            result = "";
        }
        String result1;
        if (movie.posterPath().isPresent()) {
            result1 = "http://image.tmdb.org/t/p/w300" + movie.posterPath().get();
        } else {
            result1 = "";
        }
        return MovieSearchResultViewState.builder()
                .movieId(movie.id())
                .title(movie.title())
                .yearReleased(result)
                .rating("&#9734; " + movie.rating())
                .posterUrl(result1)
                .build();
    }

    public MovieViewState toMovieViewState(Movie movie) {
        Optional<LocalDate> date = movie.releaseDate();
        String result;
        if (date.isPresent()) {
            result = Integer.toString(date.get().getYear());
        } else {
            result = "";
        }
        String result1;
        if (movie.posterPath().isPresent()) {
            result1 = "http://image.tmdb.org/t/p/w300" + movie.posterPath().get();
        } else {
            result1 = "";
        }
        return MovieViewState.builder()
                .movieId(movie.id())
                .title(movie.title())
                .yearReleased(result)
                .rating("&#9734; " + movie.rating())
                .posterUrl(result1)
                .overview(movie.overview().get())
                .build();
    }

}
