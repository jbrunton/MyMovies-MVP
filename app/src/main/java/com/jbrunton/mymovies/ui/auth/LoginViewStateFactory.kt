package com.jbrunton.mymovies.ui.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.usecases.auth.LoginState

class LoginViewStateFactory {
    companion object {
        fun from(result: AsyncResult<LoginState>): LoadingViewState<LoginViewState> {
            return LoadingViewState.build(LoginViewState.Empty).flatMap(result, this::transform)
        }

        private fun transform(state: LoginState): AsyncResult<LoginViewState> = when (state) {
            is LoginState.Valid -> AsyncResult.success(LoginViewState(null, null))
            is LoginState.Invalid -> AsyncResult.success(
                    LoginViewState(
                            usernameError = if (state.requiresUsername) { "Username required" } else { null },
                            passwordError = if (state.requiresPassword) { "Password required" } else { null }
                    )
            )
        }
    }
}