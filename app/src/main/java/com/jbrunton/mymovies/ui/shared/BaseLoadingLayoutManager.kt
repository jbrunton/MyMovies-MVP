package com.jbrunton.mymovies.ui.shared

import android.view.View

abstract class BaseLoadingLayoutManager<T>(override val containerView: View, contentView: View): LayoutManager<LoadingViewState<T>> {
    val loadingLayoutManager: LoadingLayoutManager = LoadingLayoutManager.buildFor(this, contentView)

    override fun updateView(viewState: LoadingViewState<T>) {
        loadingLayoutManager.updateLayout(viewState) {
            updateContentView(it)
        }
    }

    protected abstract fun updateContentView(viewState: T)
}