package com.jbrunton.mymovies.ui.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.inject
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.shared.BaseFragment
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_genres.*
import kotlinx.android.synthetic.main.layout_account_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*


class AccountFragment : BaseFragment<AccountViewModel>() {
    private lateinit var loadingLayoutManager: LoadingLayoutManager
    private val picassoHelper = PicassoHelper()

    override val viewModel: AccountViewModel by injectViewModel()
    val navigator: Navigator by inject()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingLayoutManager = LoadingLayoutManager.buildFor(this, account_details)
    }

    override fun onBindListeners() {
        error_try_again.setOnClickListener { viewModel.retry() }
        sign_in.setOnClickListener { viewModel.showLogin(navigator) }
        sign_out.setOnClickListener { viewModel.signOut() }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(viewLifecycleOwner, this::updateView)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
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