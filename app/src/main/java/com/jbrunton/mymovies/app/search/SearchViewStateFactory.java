package com.jbrunton.mymovies.app.search;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.movies.BaseMovieViewStateFactory;
import com.jbrunton.mymovies.app.movies.MovieSearchResultViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SearchViewStateFactory extends BaseMovieViewStateFactory {
    public SearchViewState fromList(List<Movie> movies) {
        if (movies.isEmpty()) {
            return fromLoadingViewState(LoadingViewState.errorBuilder()
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

    public SearchViewState fromError(Throwable throwable) {
        DescriptiveError error = DescriptiveError.from(throwable);
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return  fromLoadingViewState(LoadingViewState.errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build());
    }



    public SearchViewState fromLoadingViewState(LoadingViewState loadingViewState) {
        return SearchViewState.builder()
                .setMovies(Collections.emptyList())
                .setLoadingViewState(loadingViewState)
                .build();
    }

    public MovieSearchResultViewState toMovieSearchResultViewState(Movie movie) {
        return MovieSearchResultViewState.builder()
                .movieId(movie.id())
                .title(movie.title())
                .yearReleased(convertReleaseDate(movie.releaseDate()))
                .rating("&#9734; " + movie.rating())
                .posterUrl(posterUrl(movie))
                .build();
    }
}
