package com.jbrunton.mymovies.helpers

import android.content.Context
import com.jbrunton.entities.repositories.ApplicationPreferences

class SharedApplicationPreferences(val context: Context) : ApplicationPreferences {
    private val preferences = context.getSharedPreferences("MyMovies", Context.MODE_PRIVATE)

    override var sessionId: String?
        get() = preferences.getString("SESSION_ID", "")
        set(value) = preferences.edit().putString("SESSION_ID", value).apply()

}