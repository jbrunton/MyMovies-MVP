package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.app.models.Movie;

import java.util.Optional;

public class MovieDetailsResponse extends BaseMovieResource {
    private String overview;

    public Movie toMovie() {
        return builder()
                .overview(Optional.of(overview))
                .build();
    }
}
