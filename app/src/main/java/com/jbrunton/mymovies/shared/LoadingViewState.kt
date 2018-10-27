package com.jbrunton.mymovies.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.networking.DescriptiveError

sealed class LoadingViewState<out T> {
    companion object {
        fun <T>fromError(throwable: Throwable): LoadingViewState<T> {
            val error = DescriptiveError.from(throwable)
            @DrawableRes val resId = if (error.isNetworkError)
                R.drawable.ic_sentiment_dissatisfied_black_24dp
            else
                R.drawable.ic_sentiment_very_dissatisfied_black_24dp

            return Failure<T>(errorMessage = error.message,
                    errorIcon = resId,
                    showTryAgainButton = true)
        }
    }

    fun updateLayout(root: View, content: View, onSuccess: (T) -> Unit) {
        val loadingIndicator = root.findViewById<View>(R.id.loading_indicator)
        val errorCase = root.findViewById<View>(R.id.error_case)
        val errorText = root.findViewById<TextView>(R.id.error_text)
        val errorTryAgain = root.findViewById<View>(R.id.error_try_again)
        val errorImage = root.findViewById<ImageView>(R.id.error_image)
        content.visibility = toVisibility(this is Success)
        loadingIndicator.visibility = toVisibility(this is Loading)
        errorCase.visibility = toVisibility(this is Failure)
        when (this) {
            is Failure -> {
                errorText.text = errorMessage
                errorTryAgain.visibility = toVisibility(showTryAgainButton)
                errorImage.setImageResource(errorIcon)
            }
            is Success -> {
                onSuccess(value)
            }
        }
    }

    data class Success<T>(val value: T): LoadingViewState<T>()

    data class Failure<T>(
            val errorMessage: String,
            @DrawableRes val errorIcon: Int,
            val showTryAgainButton: Boolean = false) : LoadingViewState<T>()

    object Loading : LoadingViewState<Nothing>()
}
