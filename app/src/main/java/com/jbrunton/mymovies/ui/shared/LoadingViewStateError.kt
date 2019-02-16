package com.jbrunton.mymovies.ui.shared

import androidx.annotation.DrawableRes
import com.jbrunton.entities.errors.NetworkError
import com.jbrunton.mymovies.R

data class LoadingViewStateError(
        override val message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)

fun NetworkError.toLoadingViewStateError() = LoadingViewStateError(
        message = "There was a problem with your connection.",
        errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)
