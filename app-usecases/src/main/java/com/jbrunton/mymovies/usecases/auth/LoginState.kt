package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.entities.models.AuthSession

sealed class LoginState {
    data class SignedIn(val session: AuthSession) : LoginState()

    object Valid : LoginState()

    data class Invalid(
            val requiresUsername: Boolean,
            val requiresPassword: Boolean
    ) : LoginState()
}
