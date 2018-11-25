package com.jbrunton.mymovies.account

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.AsyncResult
import com.jbrunton.entities.models.map
import com.jbrunton.entities.models.onError
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

    private val AuthViewState = AsyncResult.Success(AccountViewState(showAccountDetails = false, showSignInDetails = true))
}