package com.jbrunton.mymovies.ui.auth

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.helpers.observe
import com.jbrunton.mymovies.ui.shared.BaseActivity
import com.jbrunton.mymovies.ui.shared.LoadingLayoutManager
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {
    override val viewModel: LoginViewModel by injectViewModel()
    lateinit var loadingLayoutManager: LoadingLayoutManager

    companion object {
        val LOGIN_REQUEST = 1
        val LOGIN_SUCCESSFUL = 1

        fun toIntent(session: AuthSession) = Intent().apply {
            putExtra("SESSION_ID", session.sessionId)
        }

        fun fromIntent(intent: Intent) = AuthSession(intent.getStringExtra("SESSION_ID"))
    }

    override fun onCreateLayout() {
        setContentView(R.layout.activity_login)

        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        loadingLayoutManager = LoadingLayoutManager.buildFor(this, sign_in_details)
    }

    override fun onBindListeners() {
        sign_in.setOnClickListener {
            viewModel.login(username_field.text.toString(), password_field.text.toString())
        }
    }

    override fun onObserveData() {
        viewModel.viewState.observe(this, this::updateView)
        viewModel.loginSuccessful.observe(this) {
            setResult(LOGIN_SUCCESSFUL, toIntent(it))
            finish()
        }
        viewModel.loginFailure.observe(this) {
            AlertDialog.Builder(this@LoginActivity)
                    .setMessage(it)
                    .setPositiveButton("OK", { _, _ -> })
                    .create().show()
        }
    }

    fun updateView(viewState: LoadingViewState<LoginViewState>) {
        loadingLayoutManager.updateLayout(viewState) {
            username_layout.error = it.usernameError
            password_layout.error = it.passwordError
        }
    }
}