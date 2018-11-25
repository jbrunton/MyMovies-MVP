package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.AsyncResult
import io.reactivex.Observable

abstract class BaseRepository {
    protected fun <T>buildResponse(apiSource: Observable<T>, cachedValue: T? = null): DataStream<T> {
        return apiSource
                .map { success(it) }
                .onErrorReturn { error(it, cachedValue) }
                .startWith(AsyncResult.Loading(cachedValue))
    }

    private fun <T>success(value: T): AsyncResult<T> {
        return AsyncResult.Success(value)
    }

    private fun <T>error(throwable: Throwable, cachedValue: T?): AsyncResult<T> {
        return AsyncResult.Failure(throwable, cachedValue)
    }
}