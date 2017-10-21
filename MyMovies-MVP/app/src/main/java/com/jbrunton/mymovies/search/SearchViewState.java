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

    static Builder builder() {
        return new AutoValue_SearchViewState.Builder()
                .setErrorIcon(0)
                .setErrorMessage("")
                .setShowTryAgainButton(false);
    }

    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setMovies(List<SearchItemViewState> movies);
        abstract Builder setShowError(boolean showError);
        abstract Builder setErrorMessage(String errorMessage);
        abstract Builder setErrorIcon(@DrawableRes int errorIcon);
        abstract Builder setShowTryAgainButton(boolean showTryAgainButton);
        abstract SearchViewState build();
    }
}