package com.jbrunton.mymovies.ui.auth

import android.view.View
import com.jbrunton.libs.ui.BaseLoadingViewController
import com.jbrunton.mymovies.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_login.*

class LoginViewController : BaseLoadingViewController<LoginViewState>(), LayoutContainer {
    override val layout = R.layout.activity_login
    override val contentView: View get() = sign_in_details

    override fun updateContentView(viewState: LoginViewState) {
        username_layout.error = viewState.usernameError
        password_layout.error = viewState.passwordError
    }
}