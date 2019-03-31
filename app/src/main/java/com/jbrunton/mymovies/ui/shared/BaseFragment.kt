package com.jbrunton.mymovies.ui.shared

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.nav.NavigationContext
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.usecases.nav.NavigationResult
import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(),
        HasContainer, CoroutineScope, ViewModelLifecycle, NavigationContext
{
    abstract val viewModel: T
    val navigator: Navigator by inject()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        navigator.onActivityResult(requestCode, resultCode, data, this)
    }

    override fun onNavigationResult(result: NavigationResult) {
        viewModel.onNavigationResult(result)
    }

    override fun onCreateLayout() {}
    override fun onBindListeners() {
        viewModel.navigationRequest.observe(this, navigator::navigate)
    }
    override fun onObserveData() {}
}
