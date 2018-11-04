package com.jbrunton.mymovies.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.toVisibility
import java.io.IOException

class LoadingLayoutManager(root: View, val content: View) {
    val loadingIndicator = root.findViewById<View>(R.id.loading_indicator)
    val errorCase = root.findViewById<View>(R.id.error_case)
    val errorText = root.findViewById<TextView>(R.id.error_text)
    val errorTryAgain = root.findViewById<View>(R.id.error_try_again)
    val errorImage = root.findViewById<ImageView>(R.id.error_image)

    companion object {
        fun buildFor(fragment: BaseFragment<*>, content: View): LoadingLayoutManager {
            return LoadingLayoutManager(fragment.view!!, content)
        }

        fun buildFor(activity: BaseActivity<*>, content: View): LoadingLayoutManager {
            return LoadingLayoutManager(activity.findViewById(android.R.id.content), content)
        }
    }

    fun <T>updateLayout(viewState: LoadingViewState<T>, onSuccess: (T) -> Unit) {
        content.visibility = toVisibility(showContent(viewState))
        loadingIndicator.visibility = toVisibility(showLoadingIndicator(viewState))
        errorCase.visibility = toVisibility(showErrorCase(viewState))
        when (viewState) {
            is LoadingViewState.Failure -> {
                if (viewState.cachedValue == null) {
                    showErrorDetails(viewState.error)
                } else {
                    onSuccess(viewState.cachedValue)
                }
            }
            is LoadingViewState.Success -> {
                onSuccess(viewState.value)
            }
        }
    }

    protected fun <T>showContent(viewState: LoadingViewState<T>): Boolean {
        return viewState is LoadingViewState.Success
                || viewState is LoadingViewState.Failure && viewState.cachedValue != null
    }

    protected fun <T>showLoadingIndicator(viewState: LoadingViewState<T>): Boolean {
        return viewState is LoadingViewState.Loading
    }

    protected fun <T>showErrorCase(viewState: LoadingViewState<T>): Boolean {
        return viewState is LoadingViewState.Failure && viewState.cachedValue == null
    }

    protected fun showErrorDetails(error: Throwable) {
        if (error is IOException) {
            errorText.text = "There was a problem with your connection."
            errorTryAgain.visibility = View.VISIBLE
            errorImage.setImageResource(R.drawable.ic_sentiment_dissatisfied_black_24dp)
        } else if (error is LoadingViewStateError) {
            errorText.text = error.message
            errorTryAgain.visibility = toVisibility(error.allowRetry)
            errorImage.setImageResource(error.errorIcon)

        } else {
            errorText.text = "Unexpected error."
            errorTryAgain.visibility = View.GONE
            errorImage.setImageResource(R.drawable.ic_sentiment_very_dissatisfied_black_24dp)
        }
    }
}