package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnError
import com.jbrunton.async.doOnSuccess
import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.networking.parseStatusMessage
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class LoginUseCase(val repository: AccountRepository, val schedulerFactory: SchedulerFactory) {
    val loginSuccessful = PublishSubject.create<Unit>()
    val loginFailure = PublishSubject.create<Unit>()

    fun login(username: String, password: String): LoginState {
        val loginState = validate(username, password)
        if (loginState is LoginState.Valid) {
            repository.login(username, password)
                    .compose(schedulerFactory.apply())
                    .subscribe(this::onLoginResult)
        }
        return loginState
    }

    private fun validate(username: String, password: String): LoginState {
        if (username.isNotBlank() && password.isNotBlank()) {
            return LoginState.Valid
        } else {
            return LoginState.Invalid(username.isBlank(), password.isBlank())
        }
    }

    private fun onLoginResult(result: AsyncResult<AuthSession>) {
        result.doOnSuccess { loginSuccessful.onNext(it.value) }
                .handleNetworkErrors()
                .doOnNetworkError(this::handleNetworkError)
                .doOnError(HttpException::class) {
                    action(this@LoginUseCase::handleAuthFailure) whenever { it.code() == 401 }
                }
    }

    private fun handleNetworkError(result: AsyncResult.Failure<AuthSession>) {
        loginFailure.onNext("Could not connect to server - please check your connection.")
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<AuthSession>) {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.onNext(message)
    }
}