package com.jbrunton.mymovies.libs.ui

import android.view.View
import androidx.annotation.DrawableRes
import com.jbrunton.async.*
import com.jbrunton.mymovies.entities.errors.NetworkError

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
                allowRetryVisibility = if (error.allowRetry) {
                    View.VISIBLE
                } else {
                    View.GONE
                },
                contentViewState = emptyState
        )

        fun <T> failure(error: Throwable, emptyState: T) = when (error) {
            is LoadingViewStateError -> Companion.failure(error, emptyState)
            is NetworkError -> Companion.failure(error.toLoadingViewStateError(), emptyState)
            else -> throw error
        }

        fun <V> build(emptyState: V) = LoadingViewStateBuilder(emptyState)
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
            .onError(NetworkError::class) {
                use { LoadingViewState.Companion.failure(it.error, emptyState) }
            }
            .onError(LoadingViewStateError::class) {
                use { LoadingViewState.Companion.failure(it.error, emptyState) }
            }
            .get()
}
