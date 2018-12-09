package com.jbrunton.networking.repositories

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.toDataStream
import com.jbrunton.networking.resources.account.AccountResponse
import com.jbrunton.networking.resources.auth.AuthSessionRequest
import com.jbrunton.networking.resources.auth.LoginRequest
import com.jbrunton.networking.services.MovieService

class HttpAccountRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): AccountRepository {
    override var session: AuthSession? = null
        get() {
            if (field == null) {
                val sessionId = preferences.sessionId
                if (sessionId != null) {
                    field = AuthSession(sessionId)
                }
            }
            return field
        }
        private set(value) {
            field = value
            preferences.sessionId = value?.sessionId
        }

    override fun account(): DataStream<Account> {
        return service.account(session?.sessionId ?: "")
                .map(AccountResponse::toAccount)
                .toDataStream()
    }

    override fun login(username: String, password: String): DataStream<AuthSession> {
        return service.newAuthToken()
                .flatMap {
                    val loginRequest = LoginRequest(
                            username = username,
                            password = password,
                            requestToken = it.requestToken
                    )
                    service.login(loginRequest)
                }.flatMap {
                    val sessionRequest = AuthSessionRequest(
                            requestToken = it.requestToken
                    )
                    service.newSession(sessionRequest)
                }.doAfterNext{
                    session = it
                }.toDataStream()
    }

    override fun signOut() {
        session = null
    }
}
