package com.jbrunton.mymovies.usecases.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Account
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.DataStream
import retrofit2.HttpException

class AccountUseCase(val repository: AccountRepository) {
    fun account(): DataStream<AccountState> {
        return repository.account()
                .map(this::handleResult)
    }

    fun signOut() {
        repository.signOut()
    }

    private fun handleResult(result: AsyncResult<Account>): AsyncResult<AccountState> {
        return result.map { AccountState.SignedIn(it) as AccountState }
                .let(this::handleSignedOut)
                .handleNetworkErrors()
    }

    private fun handleSignedOut(result: AsyncResult<AccountState>): AsyncResult<AccountState> {
        return result.onError(HttpException::class) {
            use { AccountState.SignedOut } whenever { it.code() == 401 }
        }
    }
}
