package com.jbrunton.mymovies.ui.shared

import android.view.View
import androidx.annotation.LayoutRes
import kotlinx.android.extensions.LayoutContainer

interface ViewController<T>: LayoutContainer {
    @get:LayoutRes val layout: Int
    val context get() = containerView?.context
    fun bind(view: View)
    fun updateView(viewState: T)
}
