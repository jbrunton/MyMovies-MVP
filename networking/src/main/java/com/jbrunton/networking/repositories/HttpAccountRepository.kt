package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.toDataStream
import com.jbrunton.networking.resources.account.AccountResponse
import com.jbrunton.networking.services.MovieService

class HttpAccountRepository(private val service: MovieService): AccountRepository {
    override fun account(): DataStream<Account> {
        return service.account()
                .map(AccountResponse::toAccount)
                .toDataStream()
    }
}
