package com.jbrunton.mymovies.networking.repositories

import com.jbrunton.mymovies.entities.models.Account
import com.jbrunton.mymovies.entities.models.AuthSession
import com.jbrunton.mymovies.entities.repositories.*
import com.jbrunton.mymovies.networking.resources.auth.AuthSessionRequest
import com.jbrunton.mymovies.networking.resources.auth.LoginRequest
import com.jbrunton.mymovies.networking.services.MovieService

class HttpAccountRepository(
        private val service: MovieService,
        private val preferences: ApplicationPreferences
): AccountRepository {
    override var session: AuthSession = AuthSession.EMPTY
        get() {
            field = AuthSession(preferences.sessionId)
            return field
        }
        private set(value) {
            field = value
            preferences.sessionId = value.sessionId
        }

    override suspend fun account(): FlowDataStream<Account> {
        return buildFlowDataStream {
            service.account(session.sessionId).toAccount().apply {
                preferences.accountId = id
            }
        }
    }

    override suspend fun login(username: String, password: String): FlowDataStream<AuthSession> {
        return buildFlowDataStream {
            // Per the docs:
            // https://developers.themoviedb.org/3/authentication/how-do-i-generate-a-session-id

            // Step 1: Create a request token
            val requestToken = service.newAuthToken()

            // Step 2: Authorize the request token
            val loginRequest = LoginRequest(
                    username = username,
                    password = password,
                    requestToken = requestToken.requestToken
            )
            val loginResponse = service.login(loginRequest)

            // Step 3: Create a new session id
            val sessionRequest = AuthSessionRequest(loginResponse.requestToken)
            service.newSession(sessionRequest).apply {
                session = this
            }
        }
    }

    override fun signOut() {
        session = AuthSession.EMPTY
    }
}
