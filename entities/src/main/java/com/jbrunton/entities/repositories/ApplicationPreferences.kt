package com.jbrunton.entities.repositories

interface ApplicationPreferences {
    var sessionId: String?
    var accountId: String?
    var favorites: Set<String>?
}