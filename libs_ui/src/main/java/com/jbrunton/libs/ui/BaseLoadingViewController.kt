package com.jbrunton.libs.ui

import android.view.View
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseLoadingViewController<T>: ViewController<LoadingViewState<T>>() {
    abstract val contentView: View

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