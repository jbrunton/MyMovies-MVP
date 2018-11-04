package com.jbrunton.mymovies.account

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.buildViewState
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
        val viewState: LoadingViewState<AccountViewState> = buildViewState(state, { AccountViewState(it) }) {
            onError({
                error: HttpException -> error.code() == 401
            }) {
                LoadingViewState.Success(AccountViewState(showAccountDetails = false, showSignInDetails = true))
            }
        }
        this.viewState.postValue(viewState)
    }
}
