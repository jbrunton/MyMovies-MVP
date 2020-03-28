package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.models.AuthSession

interface AccountRepository {
    val session: AuthSession
    suspend fun account(): AsyncResult<Account>
    suspend fun login(username: String, password: String): AsyncResult<AuthSession>
    fun signOut()
}
