package com.jbrunton.mymovies.ui.auth

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.onError
import com.jbrunton.mymovies.entities.errors.onNetworkError
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.libs.ui.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.SingleLiveEvent
import com.jbrunton.mymovies.usecases.auth.LoginResult
import com.jbrunton.mymovies.usecases.auth.LoginUseCase
import com.jbrunton.mymovies.networking.parseStatusMessage
import retrofit2.HttpException

class LoginViewModel(container: Container) : BaseLoadingViewModel<LoginViewState>(container) {
    val useCase: LoginUseCase by inject { parametersOf(schedulerContext) }
    val loginSuccessful = SingleLiveEvent<AuthSession>()
    val loginFailure = SingleLiveEvent<String>()
    val viewStateFactory: LoginViewStateFactory by inject()

    fun onLoginClicked(username: String, password: String) {
        subscribe(useCase.login(username, password)) {
            viewState.postValue(viewStateFactory.viewState(handleResult(it)))
        }
    }

    private fun handleResult(result: AsyncResult<LoginResult>): AsyncResult<LoginResult> {
        return result.doOnSuccess(this::notifySuccess)
                .let(this::handleSignedOut)
                .onNetworkError(this::handleNetworkErrors)
    }

    private fun notifySuccess(result: AsyncResult.Success<LoginResult>) {
        val state = result.value
        if (state is LoginResult.SignedIn) {
            loginSuccessful.postValue(state.session)
        }
    }

    private fun handleNetworkErrors(result: AsyncResult.Failure<LoginResult>): AsyncResult<LoginResult> {
        snackbar.postValue(NetworkErrorSnackbar(retry = false))
        return AsyncResult.success(LoginResult.Valid)
    }

    private fun handleSignedOut(result: AsyncResult<LoginResult>): AsyncResult<LoginResult> {
        return result.onError(HttpException::class) {
            use(this@LoginViewModel::handleAuthFailure) whenever { it.code() == 401 }
        }
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<LoginResult>): LoginResult {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.postValue(message)
        return LoginResult.Valid
    }
}