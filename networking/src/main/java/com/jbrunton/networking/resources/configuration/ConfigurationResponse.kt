package com.jbrunton.networking.resources.configuration

import com.jbrunton.entities.models.Configuration

data class ConfigurationResponse(private val images: ConfigurationImageResource) {

    fun toModel() = Configuration(images.secureBaseUrl)

    data class ConfigurationImageResource(val secureBaseUrl: String)
}
