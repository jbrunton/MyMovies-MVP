package com.jbrunton.mymovies.ui.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.models.Account
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.handleNetworkErrors
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import retrofit2.HttpException

class AccountViewModel(private val repository: AccountRepository) : BaseLoadingViewModel<AccountViewState>() {
    override fun start() {
        loadAccount()
    }

    fun retry() {
        loadAccount()
    }

    fun signOut() {
        repository.signOut()
        this.viewState.postValue(LoadingViewState.success(AccountViewState.SignedOut))
    }

    fun showLogin(navigator: Navigator) {
        navigator.login().subscribe { loadAccount() }
    }

    private fun loadAccount() {
        subscribe(repository.account(), this::setAccountResponse)
    }

    private fun setAccountResponse(result: AsyncResult<Account>) {
        val viewState: LoadingViewState<AccountViewState> = result
                .map { AccountViewState(it) }
                .onError(HttpException::class) {
                    use { AccountViewState.SignedOut } whenever { it.code() == 401 }
                }
                .handleNetworkErrors()
                .toLoadingViewState(AccountViewState.Empty)
        this.viewState.postValue(viewState)
    }
}