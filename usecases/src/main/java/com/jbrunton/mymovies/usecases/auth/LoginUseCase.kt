package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.FlowDataStream
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class LoginUseCase(val repository: AccountRepository) {
    suspend fun login(username: String, password: String): FlowDataStream<LoginResult> {
        val invalidState = validate(username, password)
        return if (invalidState != null) {
            flowOf(AsyncResult.success(invalidState))
        } else {
            repository.login(username, password)
                    .map { handleResult(it) }
        }
    }

    private fun handleResult(result: AsyncResult<AuthSession>): AsyncResult<LoginResult> {
        return result
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