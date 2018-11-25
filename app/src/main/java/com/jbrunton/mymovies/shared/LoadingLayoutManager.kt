package com.jbrunton.mymovies.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.toVisibility

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
        onResetLayout()

        when (viewState) {
            is LoadingState.Success -> {
                content.visibility = View.VISIBLE
                onSuccess(viewState.value)
            }
            is LoadingState.Loading -> {
                onLoading(viewState, onSuccess)
            }
            is LoadingState.Failure -> {
                onFailure(viewState, onSuccess)
            }
        }
    }

    protected fun onResetLayout() {
        content.visibility = View.GONE
        loadingIndicator.visibility = View.GONE
        errorCase.visibility = View.GONE
    }

    protected fun <T>onLoading(viewState: LoadingState.Loading<T>, onSuccess: (T) -> Unit) {
        val cachedValue = viewState.cachedValue
        if (cachedValue == null) {
            loadingIndicator.visibility = View.VISIBLE
        } else {
            content.visibility = View.VISIBLE
            onSuccess(cachedValue)
        }
    }

    protected fun <T>onFailure(viewState: LoadingState.Failure<T>, onSuccess: (T) -> Unit) {
        val cachedValue = viewState.cachedValue
        if (cachedValue == null) {
            errorCase.visibility = View.VISIBLE
            onError(viewState.error)
        } else {
            content.visibility = View.VISIBLE
            onSuccess(cachedValue)
        }
    }
    
    protected fun onError(error: Throwable) {
        if (error is LoadingViewStateError) {
            errorText.text = error.message
            errorTryAgain.visibility = toVisibility(error.allowRetry)
            errorImage.setImageResource(error.errorIcon)
        } else {
            throw error
        }
    }
}