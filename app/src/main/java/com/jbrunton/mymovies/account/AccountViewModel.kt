package com.jbrunton.mymovies.account

import android.view.View
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.handleNetworkErrors
import com.jbrunton.mymovies.shared.toLoadingViewState
import retrofit2.HttpException

class AccountViewModel(private val repository: AccountRepository) : BaseLoadingViewModel<AccountViewState>() {
    override fun start() {
        loadAccount()
    }

    fun retry() {
        loadAccount()
    }

    fun login(username: String, password: String) {
        repository.login(username, password)
                .compose(applySchedulers())
                .subscribe(this::onLoginSuccess, this::setErrorResponse)
    }

    private fun loadAccount() {
        load(repository::account, this::setAccountResponse)
    }

    private fun setAccountResponse(result: AsyncResult<Account>) {
        val viewState: LoadingViewState<AccountViewState> = result
                .map { AccountViewState(it) }
                .onError(HttpException::class) {
                    map { AuthViewState } whenever { it.code() == 401 }
                }
                .handleNetworkErrors()
                .toLoadingViewState(AccountViewState())
        this.viewState.postValue(viewState)
    }

    private fun onLoginSuccess(result: AsyncResult<AuthSession>) {
        if (result is AsyncResult.Success) {
            result.get()
            retry()
        }
    }

    private val AuthViewState = AsyncResult.Success(AccountViewState(signInDetailsVisibility = View.VISIBLE))
}