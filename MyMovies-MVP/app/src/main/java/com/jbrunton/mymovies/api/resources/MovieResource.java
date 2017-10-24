package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.Movie;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class MovieResource {
    private String original_title;
    private String id;
    private String poster_path;
    private String backdrop_path;
    private LocalDate release_date;
    private String vote_average;
    private String overview;

    public Movie toMovie() {
        return new Movie(id, original_title, poster_path, backdrop_path, release_date, vote_average, overview);
    }
}
