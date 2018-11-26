package com.jbrunton.mymovies.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.jakewharton.rxbinding2.view.changeEvents
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.shared.BaseActivity
import com.jbrunton.mymovies.shared.LoadingLayoutManager
import com.jbrunton.mymovies.shared.LoadingViewState
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle
import io.reactivex.Scheduler
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.content_movie_details.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.util.concurrent.TimeUnit

class LoginActivity : BaseActivity<LoginViewModel>() {
    val viewModel: LoginViewModel by viewModel()
    val scheduler: Scheduler by inject()
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

    private fun validate(text: CharSequence) {
        viewModel.validate(username_field.text.toString(), password_field.text.toString())
    }

    fun updateView(viewState: LoadingViewState<LoginViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            username_layout.error = it.usernameError
            password_layout.error = it.passwordError
        }
    }
}