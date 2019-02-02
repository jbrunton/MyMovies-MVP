package com.jbrunton.entities.repositories

interface ApplicationPreferences {
    var sessionId: String
    var accountId: String
    var favorites: Set<String>
    fun addFavorite(movieId: String)
    fun removeFavorite(movieId: String)
}