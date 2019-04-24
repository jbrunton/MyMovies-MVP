package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.mymovies.entities.models.AuthSession

sealed class LoginResult {
    data class SignedIn(val session: AuthSession) : LoginResult()

    object Valid : LoginResult()

    data class Invalid(
            val requiresUsername: Boolean,
            val requiresPassword: Boolean
    ) : LoginResult()
}
