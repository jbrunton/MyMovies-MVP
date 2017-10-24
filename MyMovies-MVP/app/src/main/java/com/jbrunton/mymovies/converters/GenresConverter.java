package com.jbrunton.mymovies.converters;

import android.support.annotation.DrawableRes;

import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.R;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.discover.GenresViewState;
import com.jbrunton.mymovies.models.Genre;

import java.util.Collections;
import java.util.List;

public class GenresConverter {
    public GenresViewState convertGenresResponse(List<Genre> genres) {
        if (genres.isEmpty()) {
            return errorBuilder()
                    .setErrorMessage("Unexpected Error")
                    .setErrorIcon(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
                    .setShowTryAgainButton(true)
                    .build();
        } else {
            return GenresViewState.builder()
                    .setCurrentState(LoadingViewState.State.OK)
                    .setGenres(genres)
                    .build();
        }
    }

    public GenresViewState convertErrorResponse(DescriptiveError error) {
        @DrawableRes int resId = error.isNetworkError() ? R.drawable.ic_sentiment_dissatisfied_black_24dp : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;
        return errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build();
    }

    public GenresViewState.Builder errorBuilder() {
        return GenresViewState.builder()
                .setGenres(Collections.emptyList())
                .setCurrentState(LoadingViewState.State.ERROR);
    }
}
