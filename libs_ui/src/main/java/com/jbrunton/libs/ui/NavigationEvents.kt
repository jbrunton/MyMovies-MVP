package com.jbrunton.libs.ui

import com.jbrunton.entities.models.AuthSession

object SearchRequest : NavigationRequest
object DiscoverRequest : NavigationRequest
object AccountRequest : NavigationRequest

object LoginRequest : NavigationRequest
object FavoritesRequest : NavigationRequest

data class LoginSuccess(val session: AuthSession) : NavigationResult
