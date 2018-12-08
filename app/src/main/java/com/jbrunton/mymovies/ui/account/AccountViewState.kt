package com.jbrunton.mymovies.ui.account

import android.view.View
import com.jbrunton.entities.models.Account

data class AccountViewState(
        val id: String,
        val username: String,
        val name: String,
        val avatarUrl: String,
        val signInVisibility: Int,
        val signOutVisibility: Int
) {
    constructor(account: Account) : this(
            id = account.id,
            username = account.username ?: "",
            name = account.name ?: "",
            avatarUrl = account.avatarUrl ?: "",
            signInVisibility = View.GONE,
            signOutVisibility = View.VISIBLE)

    companion object {
        val Empty = AccountViewState(
                id = "",
                username = "",
                name = "",
                avatarUrl = "",
                signInVisibility = View.GONE,
                signOutVisibility = View.GONE
        )

        val SignedOut = Empty.copy(
                avatarUrl = "https://www.gravatar.com/avatar/0?d=mp",
                username = "Signed Out",
                signInVisibility = View.VISIBLE
        )
    }
}