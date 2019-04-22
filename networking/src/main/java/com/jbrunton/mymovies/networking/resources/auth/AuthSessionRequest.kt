package com.jbrunton.mymovies.networking.resources.auth

import com.google.gson.annotations.SerializedName

data class AuthSessionRequest(
        @SerializedName("request_token") val requestToken: String
)
