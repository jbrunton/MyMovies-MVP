package com.jbrunton.features.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.libs.ui.*
import com.jbrunton.mymovies.usecases.account.AccountState
import com.jbrunton.mymovies.usecases.account.AccountUseCase

class AccountViewModel(container: Container) : BaseLoadingViewModel<AccountViewState>(container) {
    val useCase: AccountUseCase by inject()

    override fun start() {
        super.start()
        //start(useCase)
        loadAccount()
    }

    override fun retry() {
        loadAccount()
    }

    override fun onNavigationResult(result: NavigationResult) {
        when (result) {
            is LoginSuccess -> loadAccount()
        }
    }

    fun onSignOutClicked() {
        useCase.signOut()
        val state = AsyncResult.success(AccountState.SignedOut)
        viewState.postValue(AccountViewStateFactory.viewState(state))
    }

    fun onSignInClicked() {
        navigator.navigate(LoginRequest)
    }

    fun onFavoritesClicked() {
        navigator.navigate(FavoritesRequest)
    }

    private fun loadAccount() {
        subscribe(useCase.account()) {
            viewState.postValue(AccountViewStateFactory.viewState(it))
        }
    }
}