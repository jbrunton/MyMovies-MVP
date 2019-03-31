package com.jbrunton.mymovies.nav

import com.jbrunton.entities.models.Genre

sealed class NavigationRequest {
    object SearchRequest : NavigationRequest()
    object DiscoverRequest : NavigationRequest()
    object AccountRequest : NavigationRequest()

    data class GenreRequest(val genre: Genre) : NavigationRequest()

    object LoginReqest : NavigationRequest()
    object FavoritesRequest : NavigationRequest()
}