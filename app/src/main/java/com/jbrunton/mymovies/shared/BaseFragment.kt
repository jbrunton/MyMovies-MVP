package com.jbrunton.mymovies.shared

import com.jbrunton.mymovies.MyMoviesApplication

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment() {
    inline fun <reified T : Any> get(): T {
        return (context as BaseActivity<*>).get()
    }
}
