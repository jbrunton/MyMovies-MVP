package com.jbrunton.mymovies.usecases.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.errors.onNetworkErrorUse
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import com.jbrunton.networking.parseStatusMessage
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class LoginUseCase(val repository: AccountRepository) : BaseUseCase() {
    val state = PublishSubject.create<AsyncResult<LoginState>>()
    val loginSuccessful = PublishSubject.create<AuthSession>()
    val loginFailure = PublishSubject.create<String>()
    val networkErrorSnackbar = PublishSubject.create<Unit>()

    fun login(username: String, password: String) {
        val loginState = validate(username, password)
        if (loginState is LoginState.Invalid) {
            state.onNext(AsyncResult.success(loginState))
        } else {
            repository.login(username, password)
                    .map(this::handleLoginResult)
                    .safelySubscribe(this, state::onNext)
        }
    }

    private fun handleLoginResult(result: AsyncResult<AuthSession>): AsyncResult<LoginState> {
        return result
                .doOnSuccess { loginSuccessful.onNext(it.value) }
                .map { LoginState.Valid }
                .handleNetworkErrors()
                .onNetworkErrorUse(this::showNetworkErrorSnackbar)
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

    private fun showNetworkErrorSnackbar(result: AsyncResult.Failure<LoginState>): LoginState {
        networkErrorSnackbar.onNext(Unit)
        return LoginState.Valid
    }
}