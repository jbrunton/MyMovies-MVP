package com.jbrunton.mymovies.account

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.buildViewState

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
        //val viewState = state
        //        .toViewState { AccountViewState(it) }
        val viewState = buildViewState<Account, AccountViewState>({ AccountViewState(it) }) {
            onFailure {
                LoadingViewState.fromError(it.throwable, it.cachedValue?.let { AccountViewState(it) })
            }
        }.build(state)
        this.viewState.postValue(viewState)
    }
}
