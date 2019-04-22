package com.jbrunton.features.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.libs.ui.BaseFragment
import com.jbrunton.libs.ui.observe
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_details.*


class AccountFragment : BaseFragment<AccountViewModel>() {
    private val viewController = AccountViewController()

    override val viewModel: AccountViewModel by injectViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(viewController.layout, container, false)
    }

    override fun onCreateLayout() {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        viewController.bind(this)
        viewController.showLoadingIndicator()
    }

    override fun onBindListeners() {
        viewController.error_try_again.setOnClickListener { viewModel.retry() }
        sign_in.setOnClickListener { viewModel.onSignInClicked() }
        sign_out.setOnClickListener { viewModel.onSignOutClicked() }
        favorites.setOnClickListener { viewModel.onFavoritesClicked() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner) { viewController.updateView(it) }
    }
}