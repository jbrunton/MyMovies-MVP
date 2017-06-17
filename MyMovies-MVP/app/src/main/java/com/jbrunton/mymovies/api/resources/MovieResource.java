package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.Movie;

public class MovieResource {
    private String original_title;

    public Movie toMovie() {
        return new Movie(original_title);
    }
}
