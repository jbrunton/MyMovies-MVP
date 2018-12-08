package com.jbrunton.mymovies.ui.shared

import android.view.View
import androidx.annotation.DrawableRes
import com.jbrunton.async.*

data class LoadingViewState<T>(
        val contentVisibility: Int = View.GONE,
        val loadingIndicatorVisibility: Int = View.GONE,
        val errorCaseVisibility: Int = View.GONE,
        val errorText: String = "",
        @DrawableRes val errorIcon: Int = 0,
        val allowRetryVisibility: Int = View.GONE,
        val contentViewState: T
) {
    companion object {
        fun <T> success(viewState: T) = LoadingViewState(
                contentVisibility = View.VISIBLE,
                contentViewState = viewState
        )

        fun <T> loading(emptyViewState: T) = LoadingViewState(
                loadingIndicatorVisibility = View.VISIBLE,
                contentViewState = emptyViewState
        )

        fun <T> failure(error: LoadingViewStateError, emptyViewState: T) = LoadingViewState(
                errorCaseVisibility = View.VISIBLE,
                errorText = error.message,
                errorIcon = error.errorIcon,
                allowRetryVisibility = if (error.allowRetry) { View.VISIBLE } else { View.GONE },
                contentViewState = emptyViewState
        )

        fun <T> failure(result: AsyncResult.Failure<LoadingViewState<T>>, emptyViewState: T) =
                failure(result.error as LoadingViewStateError, emptyViewState)
    }
}

fun <T> AsyncResult<T>.toLoadingViewState(emptyViewState: T): LoadingViewState<T> {
    return this.map { LoadingViewState.success(it) }
            .onLoading {
                it.useCachedValue().or(LoadingViewState.loading(emptyViewState))
            }
            .onFailure {
                it.useCachedValue()
            }
            .onError(LoadingViewStateError::class) {
                use { LoadingViewState.failure(it, emptyViewState) }
            }
            .get()
}
