package com.jbrunton.mymovies.ui.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.features.account.AccountViewState
import com.jbrunton.libs.ui.LoadingViewState
import com.jbrunton.mymovies.usecases.account.AccountState

object AccountViewStateFactory {
    fun viewState(result: AsyncResult<AccountState>): LoadingViewState<AccountViewState> {
        return LoadingViewState.build(com.jbrunton.features.account.AccountViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(state: AccountState): AsyncResult<AccountViewState> {
        return when (state) {
            is AccountState.SignedOut -> AsyncResult.success(AccountViewState.SignedOut)
            is AccountState.SignedIn -> AsyncResult.success(AccountViewState(state.account))
        }
    }
}