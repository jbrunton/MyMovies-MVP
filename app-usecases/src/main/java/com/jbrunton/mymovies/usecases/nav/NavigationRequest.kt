package com.jbrunton.mymovies.usecases.nav

import com.jbrunton.entities.models.AuthSession
import com.jbrunton.entities.models.Genre

sealed class NavigationRequest {
    object SearchRequest : NavigationRequest()
    object DiscoverRequest : NavigationRequest()
    object AccountRequest : NavigationRequest()

    data class GenreRequest(val genre: Genre) : NavigationRequest()

    object LoginRequest : NavigationRequest()
    object FavoritesRequest : NavigationRequest()
}

sealed class NavigationResult {
    data class LoginSuccess(val session: AuthSession) : NavigationResult()
}
