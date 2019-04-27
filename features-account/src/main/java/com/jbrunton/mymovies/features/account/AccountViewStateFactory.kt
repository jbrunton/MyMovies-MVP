package com.jbrunton.mymovies.features.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.usecases.account.AccountResult

object AccountViewStateFactory {
    fun viewState(result: AsyncResult<AccountResult>): LoadingViewState<AccountViewState> {
        return LoadingViewState.build(AccountViewState.Empty).flatMap(result, this::transform)
    }

    private fun transform(result: AccountResult): AsyncResult<AccountViewState> {
        return when (result) {
            is AccountResult.SignedOut -> AsyncResult.success(AccountViewState.SignedOut)
            is AccountResult.SignedIn -> AsyncResult.success(AccountViewState(result.account))
        }
    }
}