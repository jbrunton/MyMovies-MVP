package com.jbrunton.mymovies.libs.ui.views

import android.os.Bundle
import android.view.View
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.libs.ui.viewmodels.ViewModelLifecycle
import kotlinx.coroutines.CoroutineScope
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance
import kotlin.coroutines.CoroutineContext

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment(),
        KodeinAware, CoroutineScope, ViewModelLifecycle
{
    abstract val viewModel: T

    override val kodein: Kodein by lazy {
        (activity as? KodeinAware)?.kodein
                ?: (activity!!.application as KodeinAware).kodein
    }

//    override val coroutineContext: CoroutineContext by lazy {
//        container.get<CoroutineContext>()
//    }

    override val coroutineContext: CoroutineContext by instance()

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
