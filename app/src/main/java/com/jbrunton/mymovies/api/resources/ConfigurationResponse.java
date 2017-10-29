package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.Configuration;

public class ConfigurationResponse {
    private ConfigurationImageResource images;

    public Configuration toModel() {
        return Configuration.create(images.secureBaseUrl);
    }

    static class ConfigurationImageResource {
        private String secureBaseUrl;
    }
}
