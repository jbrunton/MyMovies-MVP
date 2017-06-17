package com.jbrunton.mymovies;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class Movie {
    private final String title;
    private final String posterPath;
    private final String backdropPath;
    private final LocalDate releaseDate;
    private final String rating;

    public Movie(String title, String posterPath, String backdropPath, LocalDate releaseDate, String rating) {
        this.title = title;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public String getRating() {
        return rating;
    }
}
