package com.jbrunton.mymovies.shared

import com.jbrunton.entities.models.AsyncResult
import com.jbrunton.entities.models.onError
import com.jbrunton.mymovies.R
import java.io.IOException

inline fun<T> AsyncResult<T>.onNetworkError(crossinline errorHandler: (IOException) -> AsyncResult<T>): AsyncResult<T> {
    return this.onError(IOException::class) {
        map { errorHandler(it.error as IOException) }
    }
}

fun <T> AsyncResult<T>.handleNetworkErrors(allowRetry: Boolean = true): AsyncResult<T> {
    return this.onNetworkError { networkFailure(allowRetry) }
}

fun <T>networkFailure(allowRetry: Boolean = true): AsyncResult<T> {
    val error = LoadingViewStateError(
            message = "There was a problem with your connection.",
            errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
            allowRetry = allowRetry
    )
    return AsyncResult.Failure(error)
}
