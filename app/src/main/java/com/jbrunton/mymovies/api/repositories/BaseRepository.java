package com.jbrunton.mymovies.api.repositories;

import com.jbrunton.mymovies.api.MaybeError;

import java.io.IOException;
import java.util.function.Function;

import retrofit2.adapter.rxjava2.Result;

public class BaseRepository {
    protected <T, R> MaybeError<T> fromResult(Result<R> result, Function<R, T> transformer) {
        if (result.isError()) {
            Throwable throwable = result.error();
            if (throwable instanceof IOException) {
                return MaybeError.fromErrorMessage("There was a problem with your connection.", true);
            } else {
                return MaybeError.fromErrorMessage("There was an unknown error.", false);
            }
        } else {
            T value = transformer.apply(result.response().body());
            return MaybeError.fromValue(value);
        }
    }
}
