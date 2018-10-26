package com.jbrunton.mymovies.shared;

import com.google.auto.value.AutoValue;

import androidx.annotation.DrawableRes;

@AutoValue
public abstract class LegacyLoadingViewState {
    public enum State {
        OK,
        LOADING,
        ERROR
    }

    public static final LegacyLoadingViewState OK_STATE = LegacyLoadingViewState.builder()
            .setCurrentState(State.OK)
            .build();

    public static final LegacyLoadingViewState LOADING_STATE = LegacyLoadingViewState.builder()
            .setCurrentState(State.LOADING)
            .build();

    public boolean showLoadingIndicator() {
        return currentState() == State.LOADING;
    }

    public boolean showError() {
        return currentState() == State.ERROR;
    }

    public boolean showContent() {
        return currentState() == State.OK;
    }

    public abstract State currentState();
    public abstract String errorMessage();
    public abstract @DrawableRes int errorIcon();
    public abstract boolean showTryAgainButton();

    public static Builder builder() {
        return new AutoValue_LegacyLoadingViewState.Builder()
                .setErrorIcon(0)
                .setErrorMessage("")
                .setShowTryAgainButton(false);
    }

    public static Builder errorBuilder() {
        return builder()
                .setCurrentState(State.ERROR);
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setCurrentState(State currentState);
        public abstract Builder setErrorMessage(String errorMessage);
        public abstract Builder setErrorIcon(@DrawableRes int errorIcon);
        public abstract Builder setShowTryAgainButton(boolean showTryAgainButton);

        public abstract LegacyLoadingViewState build();
    }
}