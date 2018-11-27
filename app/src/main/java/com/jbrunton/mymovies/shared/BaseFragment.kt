package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import org.koin.core.parameter.emptyParameterDefinition
import kotlin.reflect.KClass

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment() {
    fun <T : Any> resolve(klass: KClass<T>, parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return (context as BaseActivity<*>).resolve(klass, parameters)
    }

    inline fun <reified T: ViewModel> resolve(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return resolve(T::class, parameters)
    }

    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return (context as BaseActivity<*>).resolveViewModel(parameters)
    }
}
