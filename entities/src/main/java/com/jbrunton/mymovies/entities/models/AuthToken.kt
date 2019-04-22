package com.jbrunton.mymovies.entities.models

data class AuthToken(
        val success: Boolean,
        val requestToken: String
)