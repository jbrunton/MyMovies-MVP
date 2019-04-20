package com.jbrunton.libs.ui

import androidx.annotation.DrawableRes
import com.jbrunton.entities.errors.NetworkError

data class LoadingViewStateError(
        override val message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)

fun NetworkError.toLoadingViewStateError() = LoadingViewStateError(
        message = "There was a problem with your connection.",
        errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)
