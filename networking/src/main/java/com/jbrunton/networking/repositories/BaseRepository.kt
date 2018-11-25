package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.Result
import io.reactivex.Observable

abstract class BaseRepository {
    protected fun <T>buildResponse(apiSource: Observable<T>, cachedValue: T? = null): DataStream<T> {
        return apiSource
                .map { success(it) }
                .onErrorReturn { error(it, cachedValue) }
                .startWith(Result.Loading(cachedValue))
    }

    private fun <T>success(value: T): Result<T> {
        return Result.Success(value)
    }

    private fun <T>error(throwable: Throwable, cachedValue: T?): Result<T> {
        return Result.Failure(throwable, cachedValue)
    }
}