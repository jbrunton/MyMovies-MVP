package com.jbrunton.mymovies.ui.shared

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnError
import com.jbrunton.async.onError
import com.jbrunton.mymovies.R
import java.io.IOException

fun<T> AsyncResult<T>.onNetworkError(errorHandler: (AsyncResult.Failure<T>) -> AsyncResult<T>): AsyncResult<T> {
    return this.onError(IOException::class) {
        map(errorHandler)
    }
}

fun<T> AsyncResult<T>.onNetworkErrorUse(errorHandler: (AsyncResult.Failure<T>) -> T): AsyncResult<T> {
    return this.onError(IOException::class) {
        use(errorHandler)
    }
}

fun <T> AsyncResult<T>.doOnNetworkError(errorHandler: (AsyncResult.Failure<T>) -> Unit): AsyncResult<T> {
    return this.doOnError(IOException::class) {
        action(errorHandler)
    }
}

fun <T> AsyncResult<T>.handleNetworkErrors(allowRetry: Boolean = true): AsyncResult<T> {
    return this.onError(IOException::class) {
        map { networkFailure(allowRetry, it.cachedValue) }
    }
}

fun networkError(allowRetry: Boolean = true) = LoadingViewStateError(
        message = "There was a problem with your connection.",
        errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)

fun <T> networkFailure(allowRetry: Boolean = true, cachedResult: T? = null): AsyncResult<T> {
    return AsyncResult.Failure(networkError(allowRetry), cachedResult)
}
