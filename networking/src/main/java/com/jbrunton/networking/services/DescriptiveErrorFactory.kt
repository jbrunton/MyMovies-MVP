package com.jbrunton.networking.services

import com.jbrunton.networking.DescriptiveError
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class DescriptiveErrorFactory : CallAdapter.Factory() {

    override fun get(returnType: Type, annotations: Array<Annotation>,
                     retrofit: Retrofit): CallAdapter<*, *>? {
        if (CallAdapter.Factory.getRawType(returnType) != Observable::class.java) {
            return null
        }

        val delegate = retrofit.nextCallAdapter(this,
                returnType, annotations) as CallAdapter<Any, Observable<Any>>

        return DescriptiveErrorCallAdapter<Any>(delegate)
    }

    private class DescriptiveErrorCallAdapter<T> constructor(val delegate: CallAdapter<Any, Observable<T>>) : CallAdapter<Any, Observable<T>> {

        override fun responseType(): Type {
            return delegate.responseType()
        }

        override fun adapt(call: Call<Any>): Observable<T> {
            val observable = delegate.adapt(call)
            return observable.onErrorResumeNext { throwable: Throwable ->
                if (throwable is IOException) {
                    Observable.error<T>(
                            DescriptiveError(
                                    "There was a problem with your connection.",
                                    throwable,
                                    true))
                } else {
                    Observable.error<T>(DescriptiveError.from(throwable))
                }
            }
        }
    }

    companion object {
        fun create(): CallAdapter.Factory {
            return DescriptiveErrorFactory()
        }
    }
}