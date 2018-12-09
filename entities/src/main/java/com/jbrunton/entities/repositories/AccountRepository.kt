package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.AuthSession

interface AccountRepository {
    val session: AuthSession?
    fun account(): DataStream<Account>
    fun login(username: String, password: String): DataStream<AuthSession>
    fun signOut()
}
