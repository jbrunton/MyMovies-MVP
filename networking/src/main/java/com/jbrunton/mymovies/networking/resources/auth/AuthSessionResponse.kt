package com.jbrunton.mymovies.networking.resources.auth

import com.jbrunton.mymovies.entities.models.AuthSession

data class AuthSessionResponse(
        val session_id: String?
) {
   fun toAuthSession() = AuthSession(session_id!!)
}
