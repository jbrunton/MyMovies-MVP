package com.jbrunton.mymovies.ui.auth

import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.parametersOf
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.SingleLiveEvent
import com.jbrunton.mymovies.usecases.auth.LoginUseCase

class LoginViewModel(container: Container) : BaseLoadingViewModel<LoginViewState>(container) {
    val useCase: LoginUseCase by inject { parametersOf(schedulerContext) }
    val loginSuccessful = SingleLiveEvent<AuthSession>()
    val loginFailure = SingleLiveEvent<String>()
    val viewStateFactory: LoginViewStateFactory by inject()

    override fun start() {
        subscribe(useCase.state) {
            viewState.postValue(viewStateFactory.viewState(it))
        }
        subscribe(useCase.loginSuccessful) { loginSuccessful.postValue(it) }
        subscribe(useCase.loginFailure) { loginFailure.postValue(it) }
        subscribe(useCase.networkErrorSnackbar) {
            snackbar.postValue(NetworkErrorSnackbar(retry = false))
        }
        useCase.start(schedulerContext)
    }

    fun login(username: String, password: String) {
        useCase.login(username, password)
    }
}