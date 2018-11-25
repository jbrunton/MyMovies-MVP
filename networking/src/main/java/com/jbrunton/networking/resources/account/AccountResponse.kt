package com.jbrunton.networking.resources.account

import com.jbrunton.entities.models.Account

data class AccountResponse(
        val id: String,
        val username: String?,
        val name: String?,
        val avatar: AvatarResource?
) {
    fun toAccount() = Account(id, username, name, avatar?.let { it.toUrl() })

    data class AvatarResource(
            val gravatar: GravatarResource?
    ) {
        fun toUrl(): String? = gravatar?.let { it.toUrl() }
    }

    data class GravatarResource(
            val hash: String?
    ) {
        fun toUrl(): String? = hash?.let { "https://secure.gravatar.com/avatar/${hash}.jpg?s=96" }
    }

}
