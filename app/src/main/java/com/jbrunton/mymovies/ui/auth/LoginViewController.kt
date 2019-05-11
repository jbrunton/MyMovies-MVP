package com.jbrunton.mymovies.ui.auth

import android.view.View
import com.google.android.material.textfield.TextInputLayout
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.libs.kotterknife.bindView
import com.jbrunton.mymovies.libs.ui.controllers.BaseLoadingViewController

class LoginViewController : BaseLoadingViewController<LoginViewState>() {
    override val contentView: View get() = sign_in_details

    val sign_in_details: View by bindView(R.id.sign_in_details)
    val username_layout: TextInputLayout by bindView(R.id.username_layout)
    val password_layout: TextInputLayout by bindView(R.id.password_layout)

    override fun updateContentView(viewState: LoginViewState) {
        username_layout.error = viewState.usernameError
        password_layout.error = viewState.passwordError
    }
}