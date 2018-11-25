package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.models.AuthToken

interface AccountRepository {
    fun account(): DataStream<Account>
    fun login(username: String, password: String): DataStream<AuthSession>
}
