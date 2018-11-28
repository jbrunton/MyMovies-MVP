package com.jbrunton.mymovies.ui.shared

import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(), HasContainer {
    override val container: Container by lazy {
        (activity as? HasContainer)?.container
                ?: (activity!!.application as HasContainer).container
    }

//    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
//        return container.resolveViewModel(this, T::class, parameters)
//    }
}
