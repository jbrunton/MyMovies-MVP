package com.jbrunton.mymovies.ui.shared

import android.view.View
import androidx.annotation.DrawableRes
import com.jbrunton.async.*
import com.jbrunton.async.AsyncResult.Companion.success

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

        fun <T> loading(defaultViewState: T) = LoadingViewState(
                loadingIndicatorVisibility = View.VISIBLE,
                contentViewState = defaultViewState
        )

        fun <T> failure(error: LoadingViewStateError, defaultViewState: T) = LoadingViewState(
                errorCaseVisibility = View.VISIBLE,
                errorText = error.message,
                errorIcon = error.errorIcon,
                allowRetryVisibility = if (error.allowRetry) { View.VISIBLE } else { View.GONE },
                contentViewState = defaultViewState
        )
    }
}

fun <T> AsyncResult<T>.toLoadingViewState(defaultViewState: T): LoadingViewState<T> {
    val loadingViewState = LoadingViewState.loading(defaultViewState)
    val errorViewState = { error: LoadingViewStateError ->
        LoadingViewState.failure(error, defaultViewState)
    }
    return this
            .map { LoadingViewState.success(it) }
            .onLoading { it.useCachedValue().or(success(loadingViewState)) }
            .onFailure { it.useCachedValue() }
            .onError(LoadingViewStateError::class) {
                map { success(errorViewState(it.error as LoadingViewStateError)) }
            }.get()
}