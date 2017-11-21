package com.jbrunton.entities;

import com.google.auto.value.AutoValue;

import org.joda.time.LocalDate;

import java.util.Optional;

@AutoValue
public abstract class Movie {
    public abstract String id();
    public abstract String title();
    public abstract Optional<String> posterUrl();
    public abstract Optional<String> backdropUrl();
    public abstract Optional<LocalDate> releaseDate();
    public abstract String rating();
    public abstract Optional<String> overview();

    public static Builder builder() {
        return new AutoValue_Movie.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder id(String id);
        public abstract Builder title(String title);
        public abstract Builder posterUrl(Optional<String> posterUrl);
        public abstract Builder backdropUrl(Optional<String> backdropUrl);
        public abstract Builder releaseDate(Optional<LocalDate> releaseDate);
        public abstract Builder rating(String rating);
        public abstract Builder overview(Optional<String> overview);

        public abstract Movie build();
    }
}
