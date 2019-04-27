package com.jbrunton.mymovies.libs.ui.viewstates

import androidx.annotation.DrawableRes
import com.jbrunton.mymovies.entities.errors.NetworkError
import com.jbrunton.mymovies.libs.ui.R

data class LoadingViewStateError(
        override val message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)

fun NetworkError.toLoadingViewStateError() = LoadingViewStateError(
        message = "There was a problem with your connection.",
        errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
        allowRetry = allowRetry
)
