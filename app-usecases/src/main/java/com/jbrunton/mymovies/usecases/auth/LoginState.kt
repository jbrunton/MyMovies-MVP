package com.jbrunton.mymovies.usecases.auth

sealed class LoginState {
    object Valid : LoginState()

    data class Invalid(
            val requiresUsername: Boolean,
            val requiresPassword: Boolean
    ) : LoginState()
}
