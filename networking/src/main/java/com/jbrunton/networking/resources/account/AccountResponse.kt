package com.jbrunton.networking.resources.account

import com.jbrunton.entities.models.Account

data class AccountResponse(
        val id: String,
        val username: String?,
        val name: String?
) {
    fun toAccount() = Account(id, username, name)
}
