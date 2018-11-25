package com.jbrunton.mymovies.shared

import com.jbrunton.entities.models.Result
import com.jbrunton.entities.models.onError
import com.jbrunton.mymovies.R
import java.io.IOException

inline fun<T> Result<T>.onNetworkError(crossinline errorHandler: (IOException) -> Result<T>): Result<T> {
    return this.onError(IOException::class) {
        map { errorHandler(it.error as IOException) }
    }
}

fun <T> Result<T>.handleNetworkErrors(allowRetry: Boolean = true): Result<T> {
    return this.onNetworkError { networkFailure(allowRetry) }
}

fun <T>networkFailure(allowRetry: Boolean = true): Result<T> {
    val error = LoadingViewStateError(
            message = "There was a problem with your connection.",
            errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
            allowRetry = allowRetry
    )
    return Result.Failure(error)
}
