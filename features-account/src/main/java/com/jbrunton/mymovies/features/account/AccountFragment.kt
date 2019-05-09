package com.jbrunton.mymovies.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.views.BaseFragment
import kotlinx.android.synthetic.main.fragment_account.*


class AccountFragment : BaseFragment<AccountViewModel>() {
    override val viewModel: AccountViewModel by injectViewModel()
    private val viewController by lazy { AccountViewController(viewModel) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(viewController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.initializeView(this)
        viewController.showLoadingIndicator()
    }

    override fun onBindListeners() {
        viewController.bindListeners()
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewController.updateView(it) }
    }
}