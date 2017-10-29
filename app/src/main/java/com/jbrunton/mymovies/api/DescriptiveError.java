package com.jbrunton.mymovies.api;

public class DescriptiveError extends RuntimeException {
    private final boolean isNetworkError;

    public DescriptiveError(String message, boolean isNetworkError) {
        super(message);
        this.isNetworkError = isNetworkError;
    }

    public DescriptiveError(String message, Throwable cause, boolean isNetworkError) {
        super(message, cause);
        this.isNetworkError = isNetworkError;
    }

    public boolean isNetworkError() {
        return isNetworkError;
    }

    public static DescriptiveError from(Throwable throwable) {
        if (throwable instanceof DescriptiveError) {
            return (DescriptiveError) throwable;
        } else {
            return new DescriptiveError(
                    messageFromThrowable(throwable),
                    throwable,
                    false);
        }
    }

    private static String messageFromThrowable(Throwable throwable) {
        if (throwable.getMessage() == null) {
            return "Unexpected Error";
        }
        return throwable.getMessage();
    }
}