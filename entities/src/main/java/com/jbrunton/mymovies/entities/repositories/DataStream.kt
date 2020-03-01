package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.async.AsyncResult
import io.reactivex.Observable
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

typealias DataStream<T> = Observable<AsyncResult<T>>

fun <T> Observable<T>.toDataStream(cachedValue: T? = null): DataStream<T> {
    return this
            .map { AsyncResult.Success(it) as AsyncResult<T> }
            .onErrorReturn { AsyncResult.Failure(it, cachedValue) }
            .startWith(AsyncResult.Loading(cachedValue))
}

typealias FlowDataStream<T> = Flow<AsyncResult<T>>

suspend fun <T> buildFlowDataStream(cachedValue: T? = null, source: suspend () -> T): FlowDataStream<T> {
    return flow {
        emit(AsyncResult.Loading(cachedValue))
        coroutineScope {
            try {
                //delay(5000)
                val result = source()
                if (coroutineContext.isActive) {
                    emit(AsyncResult.Success(result) as AsyncResult<T>)
                }
            } catch (error: Exception) {
                emit(AsyncResult.Failure(error, cachedValue))
            }
        }
    }
}
