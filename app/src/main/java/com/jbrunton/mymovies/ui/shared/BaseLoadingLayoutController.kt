package com.jbrunton.mymovies.ui.shared

import android.view.View

abstract class BaseLoadingLayoutController<T>: LayoutController<LoadingViewState<T>> {
    lateinit var loadingLayoutManager: LoadingLayoutManager
    lateinit override var containerView: View
    abstract val contentView: View

    override fun updateView(viewState: LoadingViewState<T>) {
        loadingLayoutManager.updateLayout(viewState) {
            updateContentView(it)
        }
    }

    override fun bind(view: View) {
        this.containerView = view
        loadingLayoutManager = LoadingLayoutManager.buildFor(this, contentView)
    }

    protected abstract fun updateContentView(viewState: T)
}