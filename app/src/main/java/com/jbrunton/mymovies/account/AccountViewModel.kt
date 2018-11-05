package com.jbrunton.mymovies.account

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.models.map
import com.jbrunton.entities.models.onError
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
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

    private fun setAccountResponse(state: LoadingState<Account>) {
        val viewState: LoadingState<AccountViewState> = state
                .map { AccountViewState(it) }
                .onError(HttpException::class) {
                    map { AuthViewState } whenever { it.code() == 401 }
                }
        this.viewState.postValue(viewState)
    }

    private val AuthViewState = LoadingState.Success(AccountViewState(showAccountDetails = false, showSignInDetails = true))
}
