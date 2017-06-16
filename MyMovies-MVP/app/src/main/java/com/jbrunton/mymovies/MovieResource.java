package com.jbrunton.mymovies;

public class MovieResource {
    private String original_title;

    public Movie toMovie() {
        return new Movie(original_title);
    }
}
