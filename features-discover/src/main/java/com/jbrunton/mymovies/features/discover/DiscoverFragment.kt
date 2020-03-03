package com.jbrunton.mymovies.features.discover

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.libs.ui.controllers.rootView
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_discover.*


class DiscoverFragment : BaseFragment<DiscoverViewModel>() {
    override val viewModel: DiscoverViewModel by injectViewModel()
    private val viewController by lazy { DiscoverViewController(viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_discover, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.onViewCreated(rootView)
        viewController.showLoadingIndicator()
    }

    override fun onBindListeners() {
        viewController.error_try_again.setOnClickListener { viewModel.onRetryClicked() }
        viewController.setListener(viewModel)
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, viewController::updateView)
        viewModel.scrollToGenreResults.observe(viewLifecycleOwner) {
            view?.postDelayed({
                discover_content.smoothScrollTo(0, genres_view.bottom)
            }, 10)
        }
    }
}
