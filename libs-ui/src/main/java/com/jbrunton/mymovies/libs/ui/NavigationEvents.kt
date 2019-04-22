package com.jbrunton.mymovies.libs.ui

import com.jbrunton.mymovies.entities.models.AuthSession

object SearchRequest : NavigationRequest
object DiscoverRequest : NavigationRequest
object AccountRequest : NavigationRequest
data class MovieDetailsRequest(val movieId: String) : NavigationRequest

object LoginRequest : NavigationRequest
object FavoritesRequest : NavigationRequest

data class LoginSuccess(val session: AuthSession) : NavigationResult
