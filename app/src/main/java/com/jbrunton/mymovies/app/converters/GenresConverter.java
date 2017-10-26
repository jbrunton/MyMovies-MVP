package com.jbrunton.mymovies.app.converters;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.discover.GenresViewState;
import com.jbrunton.mymovies.app.models.Genre;

import java.util.Collections;
import java.util.List;

public class GenresConverter {
    public GenresViewState convertGenresResponse(List<Genre> genres) {
        if (genres.isEmpty()) {
            return emptyViewState(LoadingViewState.errorBuilder()
                    .setErrorMessage("Unexpected Error")
                    .setErrorIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setShowTryAgainButton(true)
                    .build());
        } else {
            return GenresViewState.builder()
                    .setLoadingViewState(
                            LoadingViewState.builder()
                                    .setCurrentState(LoadingViewState.State.OK)
                                    .build())
                    .setGenres(genres)
                    .build();
        }
    }

    public GenresViewState convertErrorResponse(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return emptyViewState(LoadingViewState.errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build());
    }

    public GenresViewState emptyViewState(LoadingViewState loadingViewState) {
        return GenresViewState.builder()
                .setGenres(Collections.emptyList())
                .setLoadingViewState(loadingViewState)
                .build();
    }
}
