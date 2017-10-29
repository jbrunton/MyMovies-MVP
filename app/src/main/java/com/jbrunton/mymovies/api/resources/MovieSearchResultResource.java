package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.Movie;

import java.util.Optional;

public class MovieSearchResultResource extends BaseMovieResource {
    public Movie toMovie() {
        return builder()
                .overview(Optional.empty())
                .build();
    }
}
