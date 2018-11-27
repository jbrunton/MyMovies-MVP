package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import org.koin.core.parameter.emptyParameterDefinition
import kotlin.reflect.KClass

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(), HasContainer {
    override val container: Container
        get() = (context as BaseActivity<*>).container

    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return container.resolveViewModel(this, T::class, parameters)
    }
}
