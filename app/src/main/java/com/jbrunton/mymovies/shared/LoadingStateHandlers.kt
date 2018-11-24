package com.jbrunton.mymovies.shared

import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.onError
import com.jbrunton.mymovies.R
import java.io.IOException

inline fun<T> LoadingState<T>.onNetworkError(crossinline errorHandler: (IOException) -> LoadingState<T>): LoadingState<T> {
    return this.onError(IOException::class) {
        map { errorHandler(it.error as IOException) }
    }
}

fun <T> LoadingState<T>.handleNetworkErrors(allowRetry: Boolean = true): LoadingState<T> {
    return this.onNetworkError { networkFailure(allowRetry) }
}

fun <T>networkFailure(allowRetry: Boolean = true): LoadingState<T> {
    val error = LoadingViewStateError(
            message = "There was a problem with your connection.",
            errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
            allowRetry = allowRetry
    )
    return LoadingState.Failure(error)
}
