package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.DataStream
import com.jbrunton.entities.models.LoadingState
import io.reactivex.Observable

abstract class BaseRepository {
    protected fun <T>buildResponse(apiSource: Observable<T>, cachedValue: T? = null): DataStream<T> {
        return apiSource
                .map { success(it) }
                .onErrorReturn { error(it, cachedValue) }
                .startWith(LoadingState.Loading(cachedValue))
    }

    private fun <T>success(value: T): LoadingState<T> {
        return LoadingState.Success(value)
    }

    private fun <T>error(throwable: Throwable, cachedValue: T?): LoadingState<T> {
        return LoadingState.Failure(throwable, cachedValue)
    }
}