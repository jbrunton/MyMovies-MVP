package com.jbrunton.mymovies.shared

import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.HasContainer
import com.jbrunton.mymovies.di.ParameterDefinition
import com.jbrunton.mymovies.di.emptyParameterDefinition

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(), HasContainer {
    override val container: Container by lazy {
        (activity as? HasContainer)?.container
                ?: (activity!!.application as HasContainer).container
    }

    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return container.resolveViewModel(this, T::class, parameters)
    }
}
