package com.jbrunton.entities.errors

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnError
import com.jbrunton.async.onError
import java.io.IOException

fun <T> AsyncResult<T>.handleNetworkErrors(allowRetry: Boolean = true): AsyncResult<T> {
    return this.onError(IOException::class) {
        map { AsyncResult.failure(networkError(allowRetry), it.cachedValue) }
    }
}

fun<T> AsyncResult<T>.onNetworkError(errorHandler: (AsyncResult.Failure<T>) -> AsyncResult<T>): AsyncResult<T> {
    return this.onError(NetworkError::class) {
        map(errorHandler)
    }
}

fun<T> AsyncResult<T>.doOnNetworkError(errorHandler: (AsyncResult.Failure<T>) -> Unit): AsyncResult<T> {
    return this.doOnError(NetworkError::class) {
        action(errorHandler)
    }
}

fun<T> AsyncResult<T>.onNetworkErrorUse(errorHandler: (AsyncResult.Failure<T>) -> T): AsyncResult<T> {
    return this.onNetworkError { AsyncResult.success(errorHandler(it)) }
}

fun networkError(allowRetry: Boolean = true) = NetworkError(allowRetry)
