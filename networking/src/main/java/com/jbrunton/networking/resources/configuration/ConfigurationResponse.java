package com.jbrunton.networking.resources.configuration;

import com.jbrunton.entities.Configuration;

public class ConfigurationResponse {
    private ConfigurationImageResource images;

    public Configuration toModel() {
        return Configuration.create(images.secureBaseUrl);
    }

    static class ConfigurationImageResource {
        private String secureBaseUrl;
    }
}
