package com.jbrunton.mymovies.shared

import android.view.View
import androidx.annotation.DrawableRes
import com.jbrunton.entities.models.AsyncResult

data class LoadingViewState<T>(
        val contentVisibility: Int = View.GONE,
        val loadingIndicatorVisibility: Int = View.GONE,
        val errorCaseVisibility: Int = View.GONE,
        val errorText: String = "",
        @DrawableRes val errorIcon: Int = 0,
        val allowRetryVisibility: Int = View.GONE,
        val contentLoadingState: T
) {
    companion object {
        fun <T> success(viewState: T) = LoadingViewState(
                contentVisibility = View.VISIBLE,
                contentLoadingState = viewState
        )

        fun <T> loading(defaultViewState: T) = LoadingViewState(
                loadingIndicatorVisibility = View.VISIBLE,
                contentLoadingState = defaultViewState
        )

        fun <T> failure(error: LoadingViewStateError, defaultViewState: T) = LoadingViewState(
                errorCaseVisibility = View.VISIBLE,
                errorText = error.message,
                errorIcon = error.errorIcon,
                allowRetryVisibility = if (error.allowRetry) { View.VISIBLE } else { View.GONE },
                contentLoadingState = defaultViewState
        )
    }
}

fun <T> AsyncResult<T>.toLoadingViewState(defaultViewState: T): LoadingViewState<T> {
    return when (this) {
        is AsyncResult.Success -> {
            LoadingViewState.success(this.value)
        }
        is AsyncResult.Loading -> {
            val cachedValue = this.cachedValue
            if (cachedValue == null) {
                LoadingViewState.loading(defaultViewState)
            } else {
                LoadingViewState.success(cachedValue)
            }
        }
        is AsyncResult.Failure -> {
            val cachedValue = this.cachedValue
            if (cachedValue == null) {
                val error = this.error
                if (error is LoadingViewStateError) {
                    LoadingViewState.failure(error, defaultViewState)
                } else {
                    throw error
                }
            } else {
                LoadingViewState.success(cachedValue)
            }
        }
    }
}