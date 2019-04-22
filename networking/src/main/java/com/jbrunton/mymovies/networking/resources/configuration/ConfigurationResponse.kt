package com.jbrunton.mymovies.networking.resources.configuration

import com.jbrunton.mymovies.entities.models.Configuration

data class ConfigurationResponse(private val images: ConfigurationImageResource) {

    fun toModel() = Configuration(images.secureBaseUrl)

    data class ConfigurationImageResource(val secureBaseUrl: String)
}
