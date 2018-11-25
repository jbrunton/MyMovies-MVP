package com.jbrunton.mymovies.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.PicassoHelper
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.helpers.toVisibility
import com.jbrunton.mymovies.shared.BaseFragment
import com.jbrunton.mymovies.shared.LoadingLayoutManager
import com.jbrunton.mymovies.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_movie_details.*
import kotlinx.android.synthetic.main.fragment_account.*
import kotlinx.android.synthetic.main.layout_account_details.*
import kotlinx.android.synthetic.main.layout_loading_state.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class AccountFragment : BaseFragment<AccountViewModel>() {
    private lateinit var loadingLayoutManager: LoadingLayoutManager
    private val picassoHelper = PicassoHelper()

    private val viewModel: AccountViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadingLayoutManager = LoadingLayoutManager.buildFor(this, account_details)
        error_try_again.clicks()
                .bindToLifecycle(this)
                .subscribe { viewModel.retry() }
//        sign_in.clicks()
//                .bindToLifecycle(this)
//                .subscribe { viewModel.login(username_field.text.toString(), password_field.text.toString()) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.viewState.observe(this, this::updateView)
        viewModel.start()
    }

    private fun updateView(viewState: LoadingViewState<AccountViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            account_username.text = it.username
            account_name.text = it.name

            picassoHelper.loadImage(context!!, avatar, it.avatarUrl)
        }
    }
}