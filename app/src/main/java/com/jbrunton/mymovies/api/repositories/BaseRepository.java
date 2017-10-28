package com.jbrunton.mymovies.api.repositories;

import com.crashlytics.android.Crashlytics;
import com.jbrunton.mymovies.models.InvalidInstantiationException;

import io.reactivex.ObservableTransformer;

public class BaseRepository {
    protected <T> ObservableTransformer<T, T> logErrors() {
        return observable -> observable.doOnError(throwable -> {
            if (throwable instanceof InvalidInstantiationException) {
                Crashlytics.logException(throwable);
            }
        });
    }
}
