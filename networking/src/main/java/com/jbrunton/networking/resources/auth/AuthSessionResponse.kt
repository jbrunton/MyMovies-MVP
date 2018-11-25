package com.jbrunton.networking.resources.auth

import com.jbrunton.entities.models.AuthSession

data class AuthSessionResponse(
        val session_id: String?
) {
   fun toAuthSession() = AuthSession(session_id!!)
}
