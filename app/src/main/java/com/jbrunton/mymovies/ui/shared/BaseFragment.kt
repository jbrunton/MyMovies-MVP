package com.jbrunton.mymovies.ui.shared

import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(), HasContainer, CoroutineScope {
    override val container: Container by lazy {
        (activity as? HasContainer)?.container
                ?: (activity!!.application as HasContainer).container
    }

    override val coroutineContext: CoroutineContext by lazy {
        container.get<CoroutineContext>()
    }
}
