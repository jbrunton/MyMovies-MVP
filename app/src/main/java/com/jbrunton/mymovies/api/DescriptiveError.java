package com.jbrunton.mymovies.api;

public class DescriptiveError {
    private final String message;
    private final boolean isNetworkError;

    public DescriptiveError(String message, boolean isNetworkError) {
        this.message = message;
        this.isNetworkError = isNetworkError;
    }

    public String getMessage() {
        return message;
    }

    public boolean isNetworkError() {
        return isNetworkError;
    }
}