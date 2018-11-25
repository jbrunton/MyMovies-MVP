package com.jbrunton.mymovies.auth

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.shared.*
import com.trello.rxlifecycle2.android.lifecycle.kotlin.bindToLifecycle

class LoginViewModel(private val repository: AccountRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<Unit>>()
    val loginSuccessful = SingleLiveEvent<Unit>()
    val loginFailure = SingleLiveEvent<String>()

    override fun start() {
    }

    fun login(username: String, password: String) {
        repository.login(username, password)
                .compose(applySchedulers())
                .subscribe(this::onLoginResult)
    }

    private fun onLoginResult(result: AsyncResult<AuthSession>) {
        val viewState: LoadingViewState<Unit> = result
                .map {
                    Unit
                }.onSuccess {
                    loginSuccessful.call()
                    it
                }.onFailure {
                    loginFailure.setValue("Auth failed")
                    AsyncResult.Success(Unit)
                }.toLoadingViewState(Unit)
        this.viewState.postValue(viewState)
    }
}