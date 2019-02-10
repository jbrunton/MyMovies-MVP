package com.jbrunton.mymovies.ui.shared

import androidx.annotation.LayoutRes
import kotlinx.android.extensions.LayoutContainer

interface LayoutManager<T>: LayoutContainer {
    @get:LayoutRes val layout: Int
    fun updateView(viewState: T)
}
