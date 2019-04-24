package com.jbrunton.mymovies.usecases.account

import com.jbrunton.mymovies.entities.models.Account

sealed class AccountResult {
    object SignedOut: AccountResult()
    data class SignedIn(val account: Account): AccountResult()
}
