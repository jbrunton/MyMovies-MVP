package com.jbrunton.mymovies.features.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.libs.ui.BaseFragment
import com.jbrunton.mymovies.libs.ui.observe
import kotlinx.android.synthetic.main.fragment_discover.*

class DiscoverFragment : BaseFragment<DiscoverViewModel>() {
    override val viewModel: DiscoverViewModel by injectViewModel()
    private val viewController: DiscoverViewController by lazy { DiscoverViewController(viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(viewController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.bind(this)
        viewController.showLoadingIndicator()
    }

    override fun onBindListeners() {
        viewController.error_try_again.setOnClickListener { viewModel.onRetryClicked() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, viewController::updateView)
    }
}
