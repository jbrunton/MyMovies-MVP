package com.jbrunton.mymovies;

import android.support.annotation.DrawableRes;

public abstract class LoadingViewState {
    public enum State {
        OK,
        LOADING,
        ERROR
    }

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

    public abstract static class Builder<T extends Builder> {
        public abstract T setCurrentState(State currentState);
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