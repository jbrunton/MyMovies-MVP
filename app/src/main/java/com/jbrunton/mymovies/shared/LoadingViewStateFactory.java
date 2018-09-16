package com.jbrunton.mymovies.shared;

import androidx.annotation.DrawableRes;

import com.jbrunton.mymovies.R;
import com.jbrunton.networking.DescriptiveError;

public class LoadingViewStateFactory {
    public LoadingViewState fromError(Throwable throwable) {
        DescriptiveError error = DescriptiveError.Companion.from(throwable);
        @DrawableRes int resId = error.isNetworkError()
                ? R.drawable.ic_sentiment_dissatisfied_black_24dp
                : R.drawable.ic_sentiment_very_dissatisfied_black_24dp;

        return LoadingViewState.errorBuilder()
                .setErrorMessage(error.getMessage())
                .setErrorIcon(resId)
                .setShowTryAgainButton(true)
                .build();
    }
}
