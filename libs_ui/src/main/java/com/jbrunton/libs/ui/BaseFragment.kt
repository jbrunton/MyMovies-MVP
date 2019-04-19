package com.jbrunton.libs.ui

import android.os.Bundle
import android.view.View
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(),
        HasContainer, CoroutineScope, ViewModelLifecycle
{
    abstract val viewModel: T

    override val container: Container by lazy {
        (activity as? HasContainer)?.container
                ?: (activity!!.application as HasContainer).container
    }

    override val coroutineContext: CoroutineContext by lazy {
        container.get<CoroutineContext>()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateLayout()
        onBindListeners()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onObserveData()
        viewModel.start()
    }

    override fun onCreateLayout() {}
    override fun onBindListeners() {}
    override fun onObserveData() {}
}
