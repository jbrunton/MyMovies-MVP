package com.jbrunton.mymovies.auth

data class LoginViewState(
        val usernameError: String? = null,
        val passwordError: String? = null
)