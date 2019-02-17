package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.errors.onNetworkErrorUse
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.networking.parseStatusMessage
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class LoginUseCase(val repository: AccountRepository) {
    val loginSuccessful = PublishSubject.create<AuthSession>()
    val retrySnackbar = PublishSubject.create<Unit>()
    val loginFailure = PublishSubject.create<String>()

    fun login(username: String, password: String): DataStream<LoginState> {
        val loginState = validate(username, password)
        if (loginState is LoginState.Valid) {
            return repository.login(username, password).map(this::handleLoginResult)
        } else {
            return Observable.just(AsyncResult.success(loginState))
        }
    }

    private fun handleLoginResult(result: AsyncResult<AuthSession>): AsyncResult<LoginState> {
        return result.doOnSuccess { loginSuccessful.onNext(it.value) }
                .map { LoginState.Valid }
                .handleNetworkErrors()
                .onNetworkErrorUse(this::showRetrySnackbar)
                .onError(HttpException::class) {
                    use(this@LoginUseCase::handleAuthFailure) whenever { it.code() == 401 }
                }
    }

    private fun validate(username: String, password: String): LoginState {
        if (username.isNotBlank() && password.isNotBlank()) {
            return LoginState.Valid
        } else {
            return LoginState.Invalid(username.isBlank(), password.isBlank())
        }
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<LoginState>): LoginState {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.onNext(message)
        return LoginState.Valid
    }

    private fun showRetrySnackbar(result: AsyncResult.Failure<LoginState>): LoginState {
        retrySnackbar.onNext(Unit)
        return LoginState.Valid
    }
}