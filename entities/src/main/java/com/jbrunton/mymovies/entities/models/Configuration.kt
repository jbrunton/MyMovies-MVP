package com.jbrunton.mymovies.entities.models

data class Configuration(val secureBaseUrl: String) {
    fun expandUrl(relativePath: String?): String? {
        return relativePath?.let {
            "${secureBaseUrl}w300${it}"
        }
    }
}
