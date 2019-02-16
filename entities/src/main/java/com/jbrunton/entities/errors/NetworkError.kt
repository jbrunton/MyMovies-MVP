package com.jbrunton.entities.errors

data class NetworkError(val allowRetry: Boolean) : RuntimeException()
