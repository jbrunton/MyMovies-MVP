package com.jbrunton.mymovies.usecases.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.DataStream
import retrofit2.HttpException

class AccountUseCase(val repository: AccountRepository) {
    fun account(): DataStream<AccountResult> {
        return repository.account()
                .map(this::handleResult)
    }

    fun signOut() {
        repository.signOut()
    }

    private fun handleResult(result: AsyncResult<Account>): AsyncResult<AccountResult> {
        return result.map { AccountResult.SignedIn(it) as AccountResult }
                .let(this::handleSignedOut)
                .handleNetworkErrors()
    }

    private fun handleSignedOut(result: AsyncResult<AccountResult>): AsyncResult<AccountResult> {
        return result.onError(HttpException::class) {
            use { AccountResult.SignedOut } whenever { it.code() == 401 }
        }
    }
}
