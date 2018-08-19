package com.jbrunton.networking.services;

import com.jbrunton.networking.DescriptiveError;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

public class DescriptiveErrorFactory extends CallAdapter.Factory {
    public static CallAdapter.Factory create() {
        return new DescriptiveErrorFactory();
    }

    @Override public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations,
                                           Retrofit retrofit) {
        if (getRawType(returnType) != Observable.class) {
            return null;
        }

        CallAdapter<Object, Observable<?>> delegate =
                (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this,
                        returnType, annotations);

        return new DescriptiveErrorRxCallAdapter(delegate);
    }

    private static class DescriptiveErrorRxCallAdapter implements CallAdapter<Object, Observable<?>> {
        private final CallAdapter<Object, Observable<?>> delegate;

        private DescriptiveErrorRxCallAdapter(CallAdapter<Object, Observable<?>> delegate) {
            this.delegate = delegate;
        }

        @Override public Type responseType() {
            return delegate.responseType();
        }

        @Override public Observable<?> adapt(Call<Object> call) {
            Observable observable = delegate.adapt(call);
            return observable.onErrorResumeNext(new Function<Throwable, Observable>() {
                @Override public Observable apply(Throwable throwable) throws Exception {
                    if (throwable instanceof IOException) {
                        return Observable.error(
                                new DescriptiveError(
                                        "There was a problem with your connection.",
                                        throwable,
                                        true));
                    } else {
                        return Observable.error(DescriptiveError.from(throwable));
                    }
                }
            });
        }
    }
}