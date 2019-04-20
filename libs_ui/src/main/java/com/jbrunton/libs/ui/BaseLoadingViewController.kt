package com.jbrunton.libs.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView

abstract class BaseLoadingViewController<T>: ViewController<LoadingViewState<T>>() {
    abstract val contentView: View

    val loading_indicator: View get() = containerView.findViewById(R.id.loading_indicator)
    val error_case: View get() = containerView.findViewById(R.id.error_case)
    val error_text: TextView get() = containerView.findViewById(R.id.error_text)
    val error_try_again: View get() = containerView.findViewById(R.id.error_try_again)
    val error_image: ImageView get() = containerView.findViewById(R.id.error_image)

    fun showLoadingIndicator() {
        contentView.visibility = View.GONE
        loading_indicator.visibility = View.VISIBLE
    }

    override fun updateView(viewState: LoadingViewState<T>) {
        contentView.visibility = viewState.contentVisibility
        loading_indicator.visibility = viewState.loadingIndicatorVisibility
        error_case.visibility = viewState.errorCaseVisibility

        error_text.text = viewState.errorText
        error_try_again.visibility = viewState.allowRetryVisibility
        error_image.setImageResource(viewState.errorIcon)

        updateContentView(viewState.contentViewState)
    }

    protected abstract fun updateContentView(viewState: T)
}