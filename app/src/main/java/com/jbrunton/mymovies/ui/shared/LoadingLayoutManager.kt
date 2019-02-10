package com.jbrunton.mymovies.ui.shared

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.R
import kotlinx.android.extensions.LayoutContainer

class LoadingLayoutManager(root: View, val content: View) {
    val loadingIndicator = root.findViewById<View>(R.id.loading_indicator)
    val errorCase = root.findViewById<View>(R.id.error_case)
    val errorText = root.findViewById<TextView>(R.id.error_text)
    val errorTryAgain = root.findViewById<View>(R.id.error_try_again)
    val errorImage = root.findViewById<ImageView>(R.id.error_image)

    init {
        content.visibility = View.GONE
        loadingIndicator.visibility = View.VISIBLE
    }

    companion object {
        fun buildFor(fragment: BaseFragment<*>, content: View): LoadingLayoutManager {
            return LoadingLayoutManager(fragment.view!!, content)
        }

        fun buildFor(activity: BaseActivity<*>, content: View): LoadingLayoutManager {
            return LoadingLayoutManager(activity.findViewById(android.R.id.content), content)
        }

        fun buildFor(container: LayoutContainer, content: View): LoadingLayoutManager {
            return LoadingLayoutManager(container.containerView!!, content)
        }
    }

    fun <T>updateLayout(viewState: LoadingViewState<T>, updateContent: (T) -> Unit) {
        content.visibility = viewState.contentVisibility
        loadingIndicator.visibility = viewState.loadingIndicatorVisibility
        errorCase.visibility = viewState.errorCaseVisibility

        errorText.text = viewState.errorText
        errorTryAgain.visibility = viewState.allowRetryVisibility
        errorImage.setImageResource(viewState.errorIcon)

        updateContent(viewState.contentViewState)
    }
}