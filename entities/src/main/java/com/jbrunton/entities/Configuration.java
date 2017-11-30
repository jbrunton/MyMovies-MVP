package com.jbrunton.entities;

import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
public abstract class Configuration {
    public abstract String secureBaseUrl();

    public static Configuration create(String secureBaseUrl) {
        return new AutoValue_Configuration(secureBaseUrl);
    }

    public Optional<String> expandUrl(String relativePath) {
        if (relativePath == null) {
            return Optional.empty();
        }
        String expandedUrl = secureBaseUrl() + "w300" + relativePath;
        return Optional.of(expandedUrl);
    }
}
