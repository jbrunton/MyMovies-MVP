package com.jbrunton.mymovies.search;

import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.Movie;

import java.util.List;

@AutoValue
public abstract class SearchViewState {
    public abstract List<SearchItemViewState> movies();
    public abstract boolean showError();
    public abstract String errorMessage();
    public abstract @DrawableRes int errorIcon();
    public abstract boolean showTryAgainButton();

    public static Builder builder() {
        return new AutoValue_SearchViewState.Builder()
                .setErrorIcon(0)
                .setErrorMessage("")
                .setShowTryAgainButton(false);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMovies(List<SearchItemViewState> movies);
        public abstract Builder setShowError(boolean showError);
        public abstract Builder setErrorMessage(String errorMessage);
        public abstract Builder setErrorIcon(@DrawableRes int errorIcon);
        public abstract Builder setShowTryAgainButton(boolean showTryAgainButton);
        public abstract SearchViewState build();
    }
}