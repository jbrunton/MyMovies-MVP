package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.AsyncResult
import io.reactivex.Observable

typealias DataStream<T> = Observable<AsyncResult<T>>

fun <T> Observable<T>.toDataStream(cachedValue: T? = null): DataStream<T> {
    return this
            .map { AsyncResult.Success(it) as AsyncResult<T> }
            .onErrorReturn { AsyncResult.Failure(it, cachedValue) }
            .startWith(AsyncResult.Loading(cachedValue))
}