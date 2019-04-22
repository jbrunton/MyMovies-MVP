package com.jbrunton.mymovies.entities.models

data class AuthSession(
        val sessionId: String
) {
    companion object {
        val EMPTY = AuthSession("")
    }
}