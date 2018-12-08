package com.jbrunton.mymovies.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.layout_account_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*


class AccountFragment : BaseFragment<AccountViewModel>() {
    private lateinit var loadingLayoutManager: LoadingLayoutManager
    private val picassoHelper = PicassoHelper()

    val viewModel: AccountViewModel by injectViewModel()
    val navigator: Navigator by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingLayoutManager = LoadingLayoutManager.buildFor(this, account_details)
        error_try_again.clicks()
                .bindToLifecycle(this)
                .subscribe { viewModel.retry() }
        sign_in.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    viewModel.showLogin(navigator)
                }
        sign_out.clicks()
                .bindToLifecycle(this)
                .subscribe { viewModel.signOut() }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observe(getViewLifecycleOwner(), this::updateView)
        viewModel.start()
    }

    private fun updateView(viewState: LoadingViewState<AccountViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            account_username.text = it.username
            account_name.text = it.name
            sign_in.visibility = it.signInVisibility
            sign_out.visibility = it.signOutVisibility

            picassoHelper.loadImage(context!!, avatar, it.avatarUrl)
        }
    }
}