package com.jbrunton.mymovies.app.moviedetails;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.movies.BaseMovieViewStateFactory;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;

public class MovieDetailsViewStateFactory extends BaseMovieViewStateFactory {
    public MovieDetailsViewState fromMovie(Movie movie) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.OK_STATE)
                .setMovie(toMovieViewState(movie))
                .build();
    }

    public MovieDetailsViewState fromError(Throwable throwable) {
        DescriptiveError error = DescriptiveError.from(throwable);
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

    private MovieViewState toMovieViewState(Movie movie) {
        return MovieViewState.builder()
                .movieId(movie.id())
                .title(movie.title())
                .yearReleased(convertReleaseDate(movie.releaseDate()))
                .rating("&#9734; " + movie.rating())
                .posterUrl(posterUrl(movie))
                .overview(movie.overview().get())
                .build();
    }
}
