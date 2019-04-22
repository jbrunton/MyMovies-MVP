package com.jbrunton.mymovies.networking.resources

import com.google.gson.annotations.SerializedName

data class HttpErrorResponse(
        @SerializedName("status_message") val statusMessage: String?
)