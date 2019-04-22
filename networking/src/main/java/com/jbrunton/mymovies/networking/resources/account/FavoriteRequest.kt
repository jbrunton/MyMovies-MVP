package com.jbrunton.mymovies.networking.resources.account

import com.google.gson.annotations.SerializedName

data class FavoriteRequest(
        @SerializedName("media_type") val mediaType: String = "movie",
        @SerializedName("media_id") val mediaId: String,
        @SerializedName("favorite") val favorite: Boolean
)