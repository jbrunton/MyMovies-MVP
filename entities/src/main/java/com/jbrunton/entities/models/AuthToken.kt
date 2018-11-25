package com.jbrunton.entities.models

import org.joda.time.DateTime

data class AuthToken(
        val success: Boolean,
        val requestToken: String
)