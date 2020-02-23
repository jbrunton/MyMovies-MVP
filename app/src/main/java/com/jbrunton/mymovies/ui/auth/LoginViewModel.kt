package com.jbrunton.mymovies.ui.auth

import androidx.lifecycle.viewModelScope
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.onError
import com.jbrunton.mymovies.entities.SchedulerContext
import com.jbrunton.mymovies.entities.errors.onNetworkError
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.usecases.auth.LoginResult
import com.jbrunton.mymovies.usecases.auth.LoginUseCase
import com.jbrunton.mymovies.networking.parseStatusMessage
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.Koin
import org.koin.core.inject
import org.koin.java.KoinJavaComponent.inject
import retrofit2.HttpException

class LoginViewModel(koin: Koin) : BaseLoadingViewModel<LoginViewState>(koin) {
    val useCase: LoginUseCase by koin.inject { org.koin.core.parameter.parametersOf(schedulerContext) }
    val loginSuccessful = SingleLiveEvent<AuthSession>()
    val loginFailure = SingleLiveEvent<String>()
    val viewStateFactory: LoginViewStateFactory by koin.inject()

    fun onLoginClicked(username: String, password: String) {
        viewModelScope.launch {
            useCase.login(username, password).collect { result ->
                viewState.postValue(viewStateFactory.viewState(handleResult(result)))
            }
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