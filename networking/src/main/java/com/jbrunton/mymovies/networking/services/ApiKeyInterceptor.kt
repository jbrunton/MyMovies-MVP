package com.jbrunton.mymovies.networking.services

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ApiKeyInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val url = original.url().newBuilder()
                .addQueryParameter("api_key", "5b07463cf2bdde60339c9621dcd6e226")
                .build()

        val request = original.newBuilder()
                .url(url)
                .build()

        return chain.proceed(request)
    }
}
