package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.repositories.AccountRepository

class LoginUseCase(val repository: AccountRepository) {
    suspend fun login(username: String, password: String): AsyncResult<LoginResult> {
        val invalidState = validate(username, password)
        if (invalidState != null) {
            return AsyncResult.success(invalidState)
        }

        return repository.login(username, password)
                .map { LoginResult.SignedIn(it) }
                .handleNetworkErrors()
    }

    private fun validate(username: String, password: String): LoginResult.Invalid? {
        if (username.isBlank() || password.isBlank()) {
            return LoginResult.Invalid(username.isBlank(), password.isBlank())
        }
        return null
    }
}