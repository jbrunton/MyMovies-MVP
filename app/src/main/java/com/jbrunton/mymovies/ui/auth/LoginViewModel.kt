package com.jbrunton.mymovies.ui.auth

import androidx.lifecycle.MutableLiveData
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.doOnSuccess
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.ui.shared.*
import com.jbrunton.networking.parseStatusMessage
import retrofit2.HttpException

class LoginViewModel(private val repository: AccountRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<LoginViewState>>()
    val loginSuccessful = SingleLiveEvent<AuthSession>()
    val loginFailure = SingleLiveEvent<String>()

    override fun start() {}

    fun login(username: String, password: String) {
        if (validate(username, password)) {
            repository.login(username, password)
                    .compose(applySchedulers())
                    .subscribe(this::onLoginResult)
        }
    }

    private fun onLoginResult(result: AsyncResult<AuthSession>) {
        val viewState: LoadingViewState<LoginViewState> = result
                .doOnSuccess { loginSuccessful.postValue(it.value) }
                .map { LoginViewState.Empty }
                .onNetworkErrorUse(this::handleNetworkError)
                .onError(HttpException::class) {
                    use(this@LoginViewModel::handleAuthFailure) whenever { it.code() == 401 }
                }
                .toLoadingViewState(LoginViewState.Empty)
        this.viewState.postValue(viewState)
    }

    private fun handleNetworkError(result: AsyncResult.Failure<LoginViewState>): LoginViewState {
        loginFailure.postValue("Could not connect to server - please check your connection.")
        return LoginViewState.Empty
    }

    private fun handleAuthFailure(result: AsyncResult.Failure<LoginViewState>): LoginViewState {
        val message = (result.error as HttpException).parseStatusMessage()
        loginFailure.postValue(message)
        return LoginViewState.Empty
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