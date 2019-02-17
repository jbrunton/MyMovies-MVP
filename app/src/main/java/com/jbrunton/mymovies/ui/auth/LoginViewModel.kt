package com.jbrunton.mymovies.ui.auth

import com.jbrunton.entities.models.AuthSession
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SingleLiveEvent
import com.jbrunton.mymovies.usecases.auth.LoginUseCase

class LoginViewModel(val useCase: LoginUseCase) : BaseLoadingViewModel<LoginViewState>() {
    val loginSuccessful = SingleLiveEvent<AuthSession>()
    val loginFailure = SingleLiveEvent<String>()

    override fun start() {
        subscribe(useCase.loginSuccessful) { loginSuccessful.postValue(it) }
        subscribe(useCase.loginFailure) { loginFailure.postValue(it) }
        subscribe(useCase.networkErrorSnackbar) {
            snackbar.postValue(NetworkErrorSnackbar(retry = false))
        }
    }

    fun login(username: String, password: String) {
        subscribe(useCase.login(username, password)) {
            viewState.postValue(LoginViewStateFactory.from(it))
        }
    }
}