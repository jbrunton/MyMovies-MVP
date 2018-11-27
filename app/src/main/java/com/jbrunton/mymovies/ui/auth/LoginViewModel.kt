package com.jbrunton.mymovies.ui.auth

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.ui.shared.*
import com.jbrunton.networking.parseStatusMessage
import retrofit2.HttpException

class LoginViewModel(private val repository: AccountRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<LoginViewState>>()
    val loginSuccessful = SingleLiveEvent<Unit>()
    val loginFailure = SingleLiveEvent<String>()

    override fun start() {}

    fun login(username: String, password: String) {
        if (validate(username, password)) {
            repository.login(username, password)
                    .compose(applySchedulers())
                    .subscribe(this::onLoginResult)
        }
    }

    private val emptyState = AsyncResult.Success(LoginViewState())

    private fun onLoginResult(result: AsyncResult<AuthSession>) {
        val viewState: LoadingViewState<LoginViewState> = result
                .map { LoginViewState() }
                .doOnSuccess { loginSuccessful.call() }
                .onNetworkError(this::handleNetworkError)
                .onError(HttpException::class) {
                    whenever { it.code() == 401 } map(this@LoginViewModel::handleAuthFailure)
                }
                .toLoadingViewState(LoginViewState())
        this.viewState.postValue(viewState)
    }

    private fun handleNetworkError(result: AsyncResult.Failure<LoginViewState>): AsyncResult<LoginViewState> {
        loginFailure.postValue("Could not connect to server - please check your connection.")
        return emptyState
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<LoginViewState>): AsyncResult<LoginViewState> {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.postValue(message)
        return emptyState
    }

    private fun validate(username: String, password: String): Boolean {
        val usernameValid = username.length > 0
        val passwordValid = password.length > 0

        if (usernameValid && passwordValid) {
            return true
        } else {
            val usernameError = if (usernameValid) null else "Username required"
            val passwordError = if (passwordValid) null else "Password required"
            val viewState = LoginViewState(
                    usernameError = usernameError,
                    passwordError = passwordError
            )
            this.viewState.postValue(LoadingViewState.success(viewState))
            return false
        }
    }
}