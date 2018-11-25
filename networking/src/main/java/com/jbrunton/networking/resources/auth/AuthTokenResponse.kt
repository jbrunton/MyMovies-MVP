package com.jbrunton.networking.resources.auth

import com.jbrunton.entities.models.Account
import com.jbrunton.entities.models.AuthToken
import org.joda.time.DateTime

data class AuthTokenResponse(
        val success: Boolean?,
        val request_token: String?
) {
    fun toAuthToken() = AuthToken(success!!, request_token!!)
}
