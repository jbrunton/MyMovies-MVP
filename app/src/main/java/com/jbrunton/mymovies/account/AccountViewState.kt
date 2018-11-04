package com.jbrunton.mymovies.account

import com.jbrunton.entities.models.Account

data class AccountViewState(
        val id: String,
        val username: String,
        val name: String
) {
    constructor(account: Account) : this(
            id = account.id,
            username = account.username ?: "<unknown username>",
            name = account.name ?: "<unknown name>")
}
