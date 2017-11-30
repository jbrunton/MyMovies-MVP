package com.jbrunton.entities;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Genre {
    public abstract String id();
    public abstract String name();

    public static Genre create(String id, String name) {
        return new AutoValue_Genre(id, name);
    }
}
