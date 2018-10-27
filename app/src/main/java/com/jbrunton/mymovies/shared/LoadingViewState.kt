package com.jbrunton.mymovies.shared

import androidx.annotation.DrawableRes
import com.jbrunton.mymovies.R
import com.jbrunton.networking.DescriptiveError

sealed class LoadingViewState<out T> {
    companion object {
        fun <T>fromError(throwable: Throwable): LoadingViewState<T> {
            val error = DescriptiveError.from(throwable)
            @DrawableRes val resId = if (error.isNetworkError)
                R.drawable.ic_sentiment_dissatisfied_black_24dp
            else
                R.drawable.ic_sentiment_very_dissatisfied_black_24dp

            return Failure(errorMessage = error.message,
                    errorIcon = resId,
                    showTryAgainButton = true)
        }
    }

    data class Success<T>(val value: T): LoadingViewState<T>()

    data class Failure<T>(
            val errorMessage: String,
            @DrawableRes val errorIcon: Int,
            val showTryAgainButton: Boolean = false) : LoadingViewState<T>()

    object Loading : LoadingViewState<Nothing>()
}
