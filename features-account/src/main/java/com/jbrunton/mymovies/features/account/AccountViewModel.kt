package com.jbrunton.mymovies.features.account

import androidx.lifecycle.viewModelScope
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.nav.FavoritesRequest
import com.jbrunton.mymovies.libs.ui.nav.LoginRequest
import com.jbrunton.mymovies.libs.ui.nav.LoginSuccess
import com.jbrunton.mymovies.libs.ui.nav.NavigationResult
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.account.AccountResult
import com.jbrunton.mymovies.usecases.account.AccountUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.inject

class AccountViewModel() : BaseLoadingViewModel<AccountViewState>() {
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
        val state = AsyncResult.success(AccountResult.SignedOut)
        viewState.postValue(AccountViewStateFactory.viewState(state))
    }

    fun onSignInClicked() {
        navigator.navigate(LoginRequest)
    }

    fun onFavoritesClicked() {
        navigator.navigate(FavoritesRequest)
    }

    private fun loadAccount() {
        viewModelScope.launch {
            useCase.account().collect { result ->
                viewState.postValue(AccountViewStateFactory.viewState(result))
            }
        }
    }
}