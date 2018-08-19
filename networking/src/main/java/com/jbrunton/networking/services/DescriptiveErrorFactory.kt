package com.jbrunton.networking.services

import com.jbrunton.networking.DescriptiveError
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.io.IOException
import java.lang.reflect.Type

class DescriptiveErrorFactory : CallAdapter.Factory() {
    override fun get(returnType: Type, annotations: Array<Annotation>,
                     retrofit: Retrofit): CallAdapter<*, *>? {
        if (CallAdapter.Factory.getRawType(returnType) == Observable::class.java) {
            val delegate = retrofit.nextCallAdapter(this,
                    returnType, annotations) as CallAdapter<Any, Observable<Any>>

            return DescriptiveErrorRxCallAdapter<Any>(delegate)
        }

        if (CallAdapter.Factory.getRawType(returnType) == Deferred::class.java) {
            val delegate = retrofit.nextCallAdapter(this,
                    returnType, annotations) as CallAdapter<Any, Deferred<Any>>

            return DescriptiveErrorDeferredCallAdapter<Any>(delegate)
        }

        return null
    }

    class DescriptiveErrorRxCallAdapter<T> constructor(private val delegate: CallAdapter<Any, Observable<T>>) : CallAdapter<Any, Observable<T>> {

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

    class DescriptiveErrorDeferredCallAdapter<T> constructor(private val delegate: CallAdapter<T, Deferred<T>>) : CallAdapter<T, Deferred<T>> {
        override fun responseType(): Type {
            return delegate.responseType()
        }

        override fun adapt(call: Call<T>): Deferred<T> {
            val deferred = delegate.adapt(call)
            return async {
                try {
                    deferred.await()
                } catch (throwable : IOException) {
                   throw DescriptiveError(
                            "There was a problem with your connection.",
                            throwable,
                            true)
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