package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.DataStream

interface AccountRepository {
    fun account(): DataStream<Account>
}
