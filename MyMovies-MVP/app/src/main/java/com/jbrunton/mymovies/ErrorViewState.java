package com.jbrunton.mymovies;

import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.search.SearchItemViewState;

import java.util.List;

public abstract class ErrorViewState {
    public abstract boolean showError();
    public abstract String errorMessage();
    public abstract @DrawableRes int errorIcon();
    public abstract boolean showTryAgainButton();

    public abstract static class Builder<T extends Builder> {
        public abstract T setShowError(boolean showError);
        public abstract T setErrorMessage(String errorMessage);
        public abstract T setErrorIcon(@DrawableRes int errorIcon);
        public abstract T setShowTryAgainButton(boolean showTryAgainButton);

        public void setDefaults() {
            setErrorIcon(0)
                    .setErrorMessage("")
                    .setShowTryAgainButton(false);
        }
    }
}