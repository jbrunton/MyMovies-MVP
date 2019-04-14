package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.mymovies.usecases.BaseUseCase
import io.reactivex.Observable

class LoginUseCase(val repository: AccountRepository) : BaseUseCase() {
    fun login(username: String, password: String): DataStream<LoginState> {
        val invalidState = validate(username, password)
        if (invalidState != null) {
            return Observable.just(AsyncResult.success(invalidState))
        } else {
            return repository.login(username, password)
                    .map(this::handleResult)
        }
    }

    private fun handleResult(result: AsyncResult<AuthSession>): AsyncResult<LoginState> {
        return result
                .map { LoginState.SignedIn(it) }
                .handleNetworkErrors()

    }

    private fun validate(username: String, password: String): LoginState.Invalid? {
        if (username.isBlank() || password.isBlank()) {
            return LoginState.Invalid(username.isBlank(), password.isBlank())
        }
        return null
    }

//    private fun handleSignedOut(result: AsyncResult<LoginState>): AsyncResult<LoginState> {
//        return result.onError(HttpException::class) {
//            use(this@LoginUseCase::handleAuthFailure) whenever { it.code() == 401 }
//        }
//    }
//
//    private fun handleAuthFailure(result: AsyncResult.Failure<LoginState>): LoginState {
//        val message = (result.error as HttpException).parseStatusMessage()
//        return LoginState.AuthFailure(message)
//    }
}