package com.jbrunton.mymovies.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {
    val viewModel: LoginViewModel by injectViewModel()
    lateinit var loadingLayoutManager: LoadingLayoutManager

    companion object {
        val LOGIN_REQUEST = 1
        val LOGIN_SUCCESSFUL = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, sign_in_details)

        viewModel.viewState.observe(this, this::updateView)
        viewModel.loginSuccessful.observe(this) {
            setResult(LOGIN_SUCCESSFUL)
            finish()
        }
        viewModel.loginFailure.observe(this) {
            AlertDialog.Builder(this@LoginActivity)
                    .setMessage(it)
                    .setPositiveButton("OK", { _, _ -> })
                    .create().show()
        }
        sign_in.clicks()
                .bindToLifecycle(this)
                .subscribe {
                    viewModel.login(username_field.text.toString(), password_field.text.toString())
                }
    }

    fun updateView(viewState: LoadingViewState<LoginViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            username_layout.error = it.usernameError
            password_layout.error = it.passwordError
        }
    }
}