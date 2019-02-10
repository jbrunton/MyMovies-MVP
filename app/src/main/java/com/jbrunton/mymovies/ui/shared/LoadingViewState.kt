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

        fun <T> loading(emptyState: T) = LoadingViewState(
                loadingIndicatorVisibility = View.VISIBLE,
                contentViewState = emptyState
        )

        fun <T> failure(error: LoadingViewStateError, emptyState: T) = LoadingViewState(
                errorCaseVisibility = View.VISIBLE,
                errorText = error.message,
                errorIcon = error.errorIcon,
                allowRetryVisibility = if (error.allowRetry) { View.VISIBLE } else { View.GONE },
                contentViewState = emptyState
        )

        fun <T> failure(result: AsyncResult.Failure<LoadingViewState<T>>, emptyState: T) =
                failure(result.error as LoadingViewStateError, emptyState)
    }
}

fun <T> AsyncResult<T>.toLoadingViewState(emptyState: T): LoadingViewState<T> {
    return this.map { LoadingViewState.success(it) }
            .onLoading {
                it.useCachedValue().or(LoadingViewState.loading(emptyState))
            }
            .onFailure {
                it.useCachedValue()
            }
            .onError(LoadingViewStateError::class) {
                use { LoadingViewState.failure(it, emptyState) }
            }
            .get()
}
