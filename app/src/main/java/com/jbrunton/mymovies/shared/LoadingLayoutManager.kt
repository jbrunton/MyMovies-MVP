package com.jbrunton.mymovies.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    fun <T>updateLayout(viewState: LoadingViewState<T>, onSuccess: (T) -> Unit) {
        content.visibility = toVisibility(viewState is LoadingViewState.Success)
        loadingIndicator.visibility = toVisibility(viewState is LoadingViewState.Loading)
        errorCase.visibility = toVisibility(viewState is LoadingViewState.Failure)
        when (viewState) {
            is LoadingViewState.Failure -> {
                errorText.text = viewState.errorMessage
                errorTryAgain.visibility = toVisibility(viewState.showTryAgainButton)
                errorImage.setImageResource(viewState.errorIcon)
            }
            is LoadingViewState.Success -> {
                onSuccess(viewState.value)
            }
        }
    }
}