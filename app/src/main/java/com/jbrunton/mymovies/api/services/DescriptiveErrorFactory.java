package com.jbrunton.mymovies.api.services;

import com.jbrunton.mymovies.api.DescriptiveError;

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

    @Override public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != Observable.class) {
            return null;
        }
        CallAdapter<Object, Observable<?>> delegate =
                (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType, annotations);

        return new CallAdapter<Object, Observable<?>>() {
            @Override public Type responseType() {
                return delegate.responseType();
            }

            @Override public Observable<?> adapt(Call<Object> call) {
                Observable o = delegate.adapt(call);
                return o.onErrorResumeNext(new Function<Throwable, Observable>() {
                    @Override public Observable apply(Throwable throwable) throws Exception {
                        if (throwable instanceof IOException) {
                            return Observable.error(new DescriptiveError("There was a problem with your connection.", throwable, true));
                        } else {
                            return Observable.error(new DescriptiveError("There was an unknown error.", throwable, false));
                        }
                    }
                });
            }
        };
    }
}