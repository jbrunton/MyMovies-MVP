package com.jbrunton.mymovies.ui.shared

import android.view.View
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseLoadingViewController<T>: ViewController<LoadingViewState<T>>() {
    abstract val contentView: View

    override fun updateView(viewState: LoadingViewState<T>) {
        contentView.visibility = viewState.contentVisibility
        loading_indicator.visibility = viewState.loadingIndicatorVisibility
        error_case.visibility = viewState.errorCaseVisibility

        error_text.text = viewState.errorText
        error_try_again.visibility = viewState.allowRetryVisibility
        error_image.setImageResource(viewState.errorIcon)

        updateContentView(viewState.contentViewState)
    }

    override fun bind(containerView: View) {
        super.bind(containerView)
        showLoadingIndicator()
    }

    protected fun showContent() {
        contentView.visibility = View.VISIBLE
        loading_indicator.visibility = View.GONE
        error_case.visibility = View.GONE
    }

    protected fun showLoadingIndicator() {
        contentView.visibility = View.GONE
        loading_indicator.visibility = View.VISIBLE
        error_case.visibility = View.GONE
    }

    protected fun showError() {
        contentView.visibility = View.GONE
        loading_indicator.visibility = View.GONE
        error_case.visibility = View.VISIBLE
    }

    protected abstract fun updateContentView(viewState: T)
}