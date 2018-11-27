package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import kotlin.reflect.KClass

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment() {
    fun <T : Any> resolve(klass: KClass<T>): T {
        return (context as BaseActivity<*>).resolve(klass)
    }

    inline fun <reified T: ViewModel> resolve(): T {
        return resolve(T::class)
    }

    inline fun <reified T: ViewModel> resolveViewModel(): T {
        return (context as BaseActivity<*>).resolveViewModel()
    }
}
