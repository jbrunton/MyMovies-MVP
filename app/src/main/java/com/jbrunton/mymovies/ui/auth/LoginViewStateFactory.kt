package com.jbrunton.mymovies.ui.auth

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.usecases.auth.LoginState

class LoginViewStateFactory(private val context: Context) {
    fun viewState(result: AsyncResult<LoginState>): LoadingViewState<LoginViewState> {
        return LoadingViewState.build(LoginViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(state: LoginState): AsyncResult<LoginViewState> = when (state) {
        is LoginState.Valid -> AsyncResult.success(LoginViewState.Valid)
        is LoginState.Invalid -> AsyncResult.success(
                LoginViewState(
                        usernameError = if (state.requiresUsername) {
                            context.getString(R.string.username_required) 
                        } else { null },
                        passwordError = if (state.requiresPassword) {
                            context.getString(R.string.password_required)
                        } else { null }
                )
        )
    }
}