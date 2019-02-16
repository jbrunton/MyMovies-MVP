package com.jbrunton.mymovies.ui.auth

import android.view.View
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewController
import kotlinx.android.synthetic.main.activity_login.*

class LoginViewController : BaseLoadingViewController<LoginViewState>() {
    override val layout = R.layout.activity_login
    override val contentView: View get() = sign_in_details

    override fun bind(containerView: View) {
        super.bind(containerView)
        showContent()
    }

    override fun updateContentView(viewState: LoginViewState) {
        username_layout.error = viewState.usernameError
        password_layout.error = viewState.passwordError
    }
}