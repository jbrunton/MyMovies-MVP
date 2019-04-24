package com.jbrunton.mymovies.ui.auth

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.libs.ui.LoadingViewState
import com.jbrunton.mymovies.usecases.auth.LoginResult

class LoginViewStateFactory(private val context: Context) {
    fun viewState(result: AsyncResult<LoginResult>): LoadingViewState<LoginViewState> {
        return LoadingViewState.build(LoginViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(result: LoginResult): AsyncResult<LoginViewState> = when (result) {
        is LoginResult.Valid -> AsyncResult.success(LoginViewState.Valid)
        is LoginResult.SignedIn -> AsyncResult.success(LoginViewState.Valid)
        is LoginResult.Invalid -> AsyncResult.success(
                LoginViewState(
                        usernameError = if (result.requiresUsername) {
                            context.getString(R.string.username_required) 
                        } else { null },
                        passwordError = if (result.requiresPassword) {
                            context.getString(R.string.password_required)
                        } else { null }
                )
        )
    }
}