package com.jbrunton.mymovies.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.clicks
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.shared.BaseActivity
import com.jbrunton.mymovies.shared.LoadingLayoutManager
import com.jbrunton.mymovies.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_movie_details.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseActivity<LoginViewModel>() {
    val viewModel: LoginViewModel by viewModel()
    lateinit var loadingLayoutManager: LoadingLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, sign_in_details)

        viewModel.viewState.observe(this, this::updateView)
        viewModel.loginSuccessful.observe(this) { finish() }
        viewModel.loginFailure.observe(this) {
            AlertDialog.Builder(this@LoginActivity)
                    .setMessage("Error signing in, please try again.")
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