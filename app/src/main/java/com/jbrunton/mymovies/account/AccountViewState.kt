package com.jbrunton.mymovies.account

import android.view.View
import com.jbrunton.entities.models.Account

data class AccountViewState(
        val id: String = "",
        val username: String = "",
        val name: String = "",
        val avatarUrl: String = "",
        val signInVisibility: Int = View.GONE
) {
    constructor(account: Account) : this(
            id = account.id,
            username = account.username ?: "",
            name = account.name ?: "",
            avatarUrl = account.avatarUrl ?: "",
            signInVisibility = View.GONE)
}