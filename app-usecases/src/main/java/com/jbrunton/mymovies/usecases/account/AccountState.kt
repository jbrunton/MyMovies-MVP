package com.jbrunton.mymovies.usecases.account

import com.jbrunton.entities.models.Account

sealed class AccountState {
    object SignedOut: AccountState()
    data class SignedIn(val account: Account): AccountState()
}
