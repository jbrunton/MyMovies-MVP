package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.Movie;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MovieResource {
    private String original_title;
    private String poster_path;
    private String backdrop_path;
    private LocalDate release_date;
    private String vote_average;

    public Movie toMovie() {
        return new Movie(original_title, poster_path, backdrop_path, release_date, vote_average);
    }
}
