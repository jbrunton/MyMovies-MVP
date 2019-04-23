package com.jbrunton.mymovies.libs.ui

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.jbrunton.mymovies.libs.kotterknife.bindView

abstract class BaseLoadingViewController<T>: ViewController<LoadingViewState<T>>() {
    abstract val contentView: View

    val loading_indicator: View by bindView(R.id.loading_indicator)
    val error_case: View by bindView(R.id.error_case)
    val error_text: TextView by bindView(R.id.error_text)
    val error_try_again: View by bindView(R.id.error_try_again)
    val error_image: ImageView by bindView(R.id.error_image)

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