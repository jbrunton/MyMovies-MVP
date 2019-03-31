package com.jbrunton.mymovies.usecases.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Account
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import com.jbrunton.mymovies.usecases.nav.NavigationRequest
import com.jbrunton.mymovies.usecases.nav.NavigationResult
import io.reactivex.subjects.PublishSubject
import retrofit2.HttpException

class AccountUseCase(val repository: AccountRepository): BaseUseCase() {
    val retrySnackbar = PublishSubject.create<Unit>()
    val state = PublishSubject.create<AsyncResult<AccountState>>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)
        load()
    }

    fun retry() {
        load()
    }

    fun signOut() {
        repository.signOut()
        state.onNext(AsyncResult.success(AccountState.SignedOut))
    }

    fun signIn() {
        navigate(NavigationRequest.LoginRequest)
    }

    override fun onNavigationResult(result: NavigationResult) {
        when (result) {
            is NavigationResult.LoginSuccess -> load()
        }
    }

    private fun load() {
        repository.account()
                .map(this::handleResult)
                .safelySubscribe(this, state::onNext)
    }

    private fun handleResult(result: AsyncResult<Account>): AsyncResult<AccountState> {
        return result.map { AccountState.SignedIn(it) as AccountState }
                .let(this::handleSignedOut)
                .let(this::handleNetworkErrors)
    }

    private fun handleSignedOut(result: AsyncResult<AccountState>): AsyncResult<AccountState> {
        return result.onError(HttpException::class) {
            use { AccountState.SignedOut } whenever { it.code() == 401 }
        }
    }

    private fun handleNetworkErrors(result: AsyncResult<AccountState>): AsyncResult<AccountState> {
        return result
                .handleNetworkErrors()
                .doOnNetworkError(this::showSnackbarIfCachedValue)
    }

    private fun showSnackbarIfCachedValue(failure: AsyncResult.Failure<AccountState>) {
        if (failure.cachedValue != null) {
            retrySnackbar.onNext(Unit)
        }
    }
}
