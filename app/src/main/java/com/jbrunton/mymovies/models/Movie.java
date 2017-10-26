package com.jbrunton.mymovies.models;

import com.google.auto.value.AutoValue;

import org.joda.time.LocalDate;

import java.util.Optional;

@AutoValue
public abstract class Movie {

    public static Movie create(String id, String title, String posterPath, LocalDate releaseDate, String rating, String overview) {
        return new AutoValue_Movie(id, title, posterPath, Optional.ofNullable(releaseDate), rating, Optional.ofNullable(overview));
    }

    public abstract String id();
    public abstract String title();
    public abstract String posterPath();
    public abstract Optional<LocalDate> getReleaseDate();
    public abstract String rating();
    public abstract Optional<String> overview();
}
