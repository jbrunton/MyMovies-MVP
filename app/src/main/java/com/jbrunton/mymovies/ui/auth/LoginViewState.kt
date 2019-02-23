package com.jbrunton.mymovies.ui.auth

data class LoginViewState(
        val usernameError: String?,
        val passwordError: String?
) {
    companion object {
        val Empty = LoginViewState(
                usernameError = null,
                passwordError = null
        )
        val Valid = Empty
    }
}