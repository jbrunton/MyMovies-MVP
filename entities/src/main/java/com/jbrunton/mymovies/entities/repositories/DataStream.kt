package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.async.AsyncResult
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.lang.Exception

typealias DataStream<T> = Flow<AsyncResult<T>>

suspend fun <T> buildDataStream(cachedValue: T? = null, source: suspend () -> T): DataStream<T> {
    return flow {
        emit(AsyncResult.Loading(cachedValue))
        coroutineScope {
            try {
                val result = source()
                if (coroutineContext.isActive) { // TODO: still need this check?
                    emit(AsyncResult.Success(result) as AsyncResult<T>)
                }
            } catch (error: Exception) {
                emit(AsyncResult.Failure(error, cachedValue))
            }
        }
    }
}
