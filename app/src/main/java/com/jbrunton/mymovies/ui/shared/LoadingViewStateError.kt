package com.jbrunton.mymovies.ui.shared

import androidx.annotation.DrawableRes
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.usecases.NetworkError

data class LoadingViewStateError(
        override val message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)

fun NetworkError.toLoadingViewStateError() = LoadingViewStateError(
        message = "There was a problem with your connection.",
        errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)