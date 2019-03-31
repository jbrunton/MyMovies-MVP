package com.jbrunton.mymovies.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*


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
        error_try_again.setOnClickListener { viewModel.retry() }
        sign_in.setOnClickListener { viewModel.signIn() }
        sign_out.setOnClickListener { viewModel.signOut() }
        favorites.setOnClickListener { navigator.navigate(NavigationRequest.FavoritesRequest) }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, viewController::updateView)
        viewModel.navigationRequest.observe(viewLifecycleOwner, navigator::navigate)
    }
}