package com.jbrunton.entities.repositories

import com.jbrunton.entities.models.Account

interface AccountRepository {
    fun account(): DataStream<Account>
}
