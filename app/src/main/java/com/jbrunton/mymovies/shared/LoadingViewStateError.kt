package com.jbrunton.mymovies.shared

import androidx.annotation.DrawableRes

class LoadingViewStateError(
        message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)
