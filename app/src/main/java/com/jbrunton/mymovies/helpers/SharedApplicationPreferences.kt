package com.jbrunton.mymovies.helpers

import android.content.Context
import com.jbrunton.entities.repositories.ApplicationPreferences

class SharedApplicationPreferences(val context: Context) : ApplicationPreferences {
    private val preferences = context.getSharedPreferences("MyMovies", Context.MODE_PRIVATE)

    override var sessionId: String
        get() = preferences.getString("SESSION_ID", "")
        set(value) = preferences.edit().putString("SESSION_ID", value).apply()

    override var accountId: String
        get() = preferences.getString("ACCOUNT_ID", "")
        set(value) = preferences.edit().putString("ACCOUNT_ID", value).apply()

    override var favorites: Set<String>?
        get() = preferences.getStringSet("FAVORITES", null)
        set(value) = preferences.edit().putStringSet("FAVORITES", value).apply()

    override fun addFavorite(movieId: String) {
        favorites = (favorites ?: emptySet()).toMutableSet().apply { add(movieId) }
    }

    override fun removeFavorite(movieId: String) {
        favorites = (favorites ?: emptySet()).toMutableSet().apply { remove(movieId) }
    }
}