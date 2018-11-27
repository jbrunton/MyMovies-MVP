package com.jbrunton.mymovies.ui.account

import android.view.View
import com.jbrunton.entities.models.*
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.ui.shared.*
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
        this.viewState.postValue(SignedOutViewState.toLoadingViewState(AccountViewState()))
    }

    fun showLogin(navigator: Navigator) {
        navigator.login().subscribe {
            retry()
        }
    }

    private fun loadAccount() {
        load(repository::account, this::setAccountResponse)
    }

    private fun setAccountResponse(result: AsyncResult<Account>) {
        val viewState: LoadingViewState<AccountViewState> = result
                .map { AccountViewState(it) }
                .onError(HttpException::class) {
                    map { SignedOutViewState } whenever { it.code() == 401 }
                }
                .handleNetworkErrors()
                .toLoadingViewState(AccountViewState())
        this.viewState.postValue(viewState)
    }

    private val SignedOutViewState = AsyncResult.Success(
            AccountViewState(
                    avatarUrl = "https://www.gravatar.com/avatar/0?d=mp",
                    username = "Signed Out",
                    signInVisibility = View.VISIBLE
            )
    )
}