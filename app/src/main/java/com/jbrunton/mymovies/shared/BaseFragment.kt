package com.jbrunton.mymovies.shared

import android.view.View

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment() {
    abstract val content: View
}
