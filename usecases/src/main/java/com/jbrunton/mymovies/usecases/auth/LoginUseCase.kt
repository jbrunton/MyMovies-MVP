package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.DataStream
import io.reactivex.Observable

class LoginUseCase(val repository: AccountRepository) {
    fun login(username: String, password: String): DataStream<LoginResult> {
        val invalidState = validate(username, password)
        if (invalidState != null) {
            return Observable.just(AsyncResult.success(invalidState))
        } else {
            return repository.login(username, password)
                    .map(this::handleResult)
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