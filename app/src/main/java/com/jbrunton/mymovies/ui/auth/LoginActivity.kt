package com.jbrunton.mymovies.ui.auth

import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.inject.injectViewModel
import com.jbrunton.mymovies.R
import com.jbrunton.libs.ui.observe
import com.jbrunton.libs.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity<LoginViewModel>() {
    override val viewModel: LoginViewModel by injectViewModel()
    private val viewController = LoginViewController()

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

        viewController.bind(this)
    }

    override fun onBindListeners() {
        sign_in.setOnClickListener {
            viewModel.onLoginClicked(username_field.text.toString(), password_field.text.toString())
        }
    }

    override fun onObserveData() {
        super.onObserveData()
        viewModel.viewState.observe(this, viewController::updateView)
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
}