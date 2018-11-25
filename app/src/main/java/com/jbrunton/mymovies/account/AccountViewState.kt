package com.jbrunton.mymovies.account

import android.view.View
import com.jbrunton.entities.models.Account

data class AccountViewState(
        val accountDetailsVisibility: Int = View.GONE,
        val signInDetailsVisibility: Int = View.GONE,
        val id: String = "",
        val username: String = "",
        val name: String = "",
        val avatarUrl: String = ""
) {
    constructor(account: Account) : this(
            accountDetailsVisibility = View.VISIBLE,
            id = account.id,
            username = account.username ?: "",
            name = account.name ?: "",
            avatarUrl = account.avatarUrl ?: "")
}