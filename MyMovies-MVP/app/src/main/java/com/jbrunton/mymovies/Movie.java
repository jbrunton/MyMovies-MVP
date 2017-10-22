package com.jbrunton.mymovies;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Optional;

public class Movie {
    private final String id;
    private final String title;
    private final String posterPath;
    private final String backdropPath;
    private final LocalDate releaseDate;
    private final String rating;

    public Movie(String id, String title, String posterPath, String backdropPath, LocalDate releaseDate, String rating) {
        this.id = id;
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public String getId() { return id; }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Optional<LocalDate> getReleaseDate() {
        return Optional.ofNullable(releaseDate);
    }

    public String getRating() {
        return rating;
    }
}
