package com.jbrunton.entities.models

data class Account(
        val id: String,
        val username: String?,
        val name: String?,
        val avatarUrl: String?
) {
    companion object {
        val Null = Account("", null, null, null)
    }
}
