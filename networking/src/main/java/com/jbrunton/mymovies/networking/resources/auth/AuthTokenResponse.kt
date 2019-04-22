package com.jbrunton.mymovies.networking.resources.auth

import com.jbrunton.mymovies.entities.models.AuthToken

data class AuthTokenResponse(
        val success: Boolean?,
        val request_token: String?
) {
    fun toAuthToken() = AuthToken(success!!, request_token!!)
}
