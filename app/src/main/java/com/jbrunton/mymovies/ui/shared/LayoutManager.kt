package com.jbrunton.mymovies.ui.shared

import android.view.View
import androidx.annotation.LayoutRes
import kotlinx.android.extensions.LayoutContainer

interface LayoutManager<T>: LayoutContainer {
    @get:LayoutRes val layout: Int
    fun bind(view: View)
    fun updateView(viewState: T)
}
