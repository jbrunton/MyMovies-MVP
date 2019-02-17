package com.jbrunton.mymovies.usecases.auth

data class LoginState(
        val usernamePresent: Boolean,
        val passwordPresent: Boolean
) {
    fun isValid() = usernamePresent && passwordPresent
}
