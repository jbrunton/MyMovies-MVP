package com.jbrunton.mymovies.ui.shared

import androidx.annotation.DrawableRes

data class LoadingViewStateError(
        override val message: String,
        @DrawableRes val errorIcon: Int = 0,
        val allowRetry: Boolean) : RuntimeException(message)
