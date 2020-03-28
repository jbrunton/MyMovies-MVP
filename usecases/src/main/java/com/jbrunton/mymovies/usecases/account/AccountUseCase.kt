package com.jbrunton.mymovies.usecases.account

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.async.onError
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.repositories.AccountRepository
import retrofit2.HttpException

class AccountUseCase(val repository: AccountRepository) {
    suspend fun account(): AsyncResult<AccountResult> {
        return repository.account()
                .map { AccountResult.SignedIn(it) as AccountResult }
                .let(this::handleSignedOut)
                .handleNetworkErrors()
    }

    fun signOut() {
        repository.signOut()
    }

    private fun handleSignedOut(result: AsyncResult<AccountResult>): AsyncResult<AccountResult> {
        return result.onError(HttpException::class) {
            use { AccountResult.SignedOut } whenever { it.code() == 401 }
        }
    }
}
