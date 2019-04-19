package com.jbrunton.mymovies.nav

import com.jbrunton.entities.models.AuthSession
import com.jbrunton.libs.ui.NavigationRequest
import com.jbrunton.libs.ui.NavigationResult

object SearchRequest : NavigationRequest
object DiscoverRequest : NavigationRequest
object AccountRequest : NavigationRequest

object LoginRequest : NavigationRequest
object FavoritesRequest : NavigationRequest

data class LoginSuccess(val session: AuthSession) : NavigationResult
