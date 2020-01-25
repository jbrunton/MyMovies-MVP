package com.jbrunton.mymovies.entities.repositories

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.models.AuthSession
import kotlinx.coroutines.flow.Flow

interface AccountRepository {
    val session: AuthSession
    suspend fun account(): FlowDataStream<Account>
    fun login(username: String, password: String): DataStream<AuthSession>
    fun signOut()
}
