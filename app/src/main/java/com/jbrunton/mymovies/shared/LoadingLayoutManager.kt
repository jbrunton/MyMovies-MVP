package com.jbrunton.mymovies.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.entities.models.LoadingState
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

    fun <T>updateLayout(viewState: LoadingState<T>, onSuccess: (T) -> Unit) {
        content.visibility = toVisibility(showContent(viewState))
        loadingIndicator.visibility = toVisibility(showLoadingIndicator(viewState))
        errorCase.visibility = toVisibility(showErrorCase(viewState))
        when (viewState) {
            is LoadingState.Failure -> {
                handleFailure(viewState, onSuccess)
            }
            is LoadingState.Success -> {
                onSuccess(viewState.value)
            }
        }
    }

    protected fun <T>handleFailure(viewState: LoadingState.Failure<T>, onSuccess: (T) -> Unit) {
        val error = viewState.error
        if (error is LoadingViewStateError) {
            val cachedValue = viewState.cachedValue
            if (cachedValue == null) {
                showErrorDetails(error)
            } else {
                onSuccess(cachedValue)
            }
        } else {
            throw UnhandledFailureException(viewState.error)
        }
    }

    protected fun <T>showContent(viewState: LoadingState<T>): Boolean {
        return viewState is LoadingState.Success
                || viewState is LoadingState.Failure && viewState.cachedValue != null
    }

    protected fun <T>showLoadingIndicator(viewState: LoadingState<T>): Boolean {
        return viewState is LoadingState.Loading
    }

    protected fun <T>showErrorCase(viewState: LoadingState<T>): Boolean {
        return viewState is LoadingState.Failure && viewState.cachedValue == null
    }

    protected fun showErrorDetails(error: LoadingViewStateError) {
        errorText.text = error.message
        errorTryAgain.visibility = toVisibility(error.allowRetry)
        errorImage.setImageResource(error.errorIcon)
    }
}