package com.jbrunton.mymovies.api;

import java.util.Objects;
import java.util.function.Consumer;


public class MaybeError<T> {
    private final T value;
    private final DescriptiveError error;

    public MaybeError(T value, DescriptiveError error) {
        this.value = value;
        this.error = error;

        if (Objects.isNull(value)) {
            Objects.requireNonNull(error);
        }
    }

    public T getValue() {
        return value;
    }

    public DescriptiveError getError() {
        return error;
    }

    public MaybeError<T> ifValue(Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
        return this;
    }

    public MaybeError<T> elseIfError(Consumer<DescriptiveError> consumer) {
        if (error != null) {
            consumer.accept(error);
        }
        return this;
    }

    public static <T> MaybeError<T> fromValue(T value) {
        return new MaybeError<T>(value, null);
    }

    public static <T> MaybeError<T> fromError(DescriptiveError error) {
        return new MaybeError<T>(null, error);
    }

    public static <T> MaybeError<T> fromErrorMessage(String message, boolean isNetworkError) {
        return fromError(new DescriptiveError(message, isNetworkError));
    }
}