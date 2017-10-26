package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.Movie;

import org.joda.time.LocalDate;

public class MovieResource {
    private String original_title;
    private String id;
    private String poster_path;
    private LocalDate release_date;
    private String vote_average;
    private String overview;

    public Movie toMovie() {
        return Movie.create(id, original_title, poster_path, release_date, vote_average, overview);
    }
}
