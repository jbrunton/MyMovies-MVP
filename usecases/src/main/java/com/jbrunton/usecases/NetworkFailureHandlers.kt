package com.jbrunton.usecases

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.onError
import java.io.IOException

data class LoadingViewStateError(
        override val message: String,
        val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)

fun<T> AsyncResult<T>.onNetworkError(errorHandler: (AsyncResult.Failure<T>) -> AsyncResult<T>): AsyncResult<T> {
    return this.onError(IOException::class) {
        map(errorHandler)
    }
}

fun<T> AsyncResult<T>.onNetworkErrorUse(errorHandler: (AsyncResult.Failure<T>) -> T): AsyncResult<T> {
    return this.onNetworkError { AsyncResult.success(errorHandler(it)) }
}

fun <T> AsyncResult<T>.handleNetworkErrors(allowRetry: Boolean = true): AsyncResult<T> {
    return this.onNetworkError {
        AsyncResult.failure(networkError(allowRetry), it.cachedValue)
    }
}

fun networkError(allowRetry: Boolean = true) = LoadingViewStateError(
        message = "There was a problem with your connection.",
        //errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)
