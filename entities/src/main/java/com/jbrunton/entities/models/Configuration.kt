package com.jbrunton.entities.models

import com.google.common.base.Optional

data class Configuration(val secureBaseUrl: String) {
    fun expandUrl(relativePath: String?): Optional<String> {
        if (relativePath == null) {
            return Optional.absent()
        }
        val expandedUrl = secureBaseUrl + "w300" + relativePath
        return Optional.of(expandedUrl)
    }
}
