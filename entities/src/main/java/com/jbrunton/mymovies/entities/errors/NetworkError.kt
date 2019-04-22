package com.jbrunton.mymovies.entities.errors

data class NetworkError(val allowRetry: Boolean) : RuntimeException()
