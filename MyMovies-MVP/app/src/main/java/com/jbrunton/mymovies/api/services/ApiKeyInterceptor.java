package com.jbrunton.mymovies.api.services;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ApiKeyInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        HttpUrl url = original.url().newBuilder()
                .addQueryParameter("api_key", "5b07463cf2bdde60339c9621dcd6e226")
                .build();

        Request request = original.newBuilder()
                .url(url)
                .build();

        return chain.proceed(request);
    }
}
