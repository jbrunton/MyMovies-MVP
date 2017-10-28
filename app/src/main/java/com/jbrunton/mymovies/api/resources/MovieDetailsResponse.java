package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.InvalidInstantiationException;
import com.jbrunton.mymovies.models.Movie;

import java.util.Optional;

public class MovieDetailsResponse extends BaseMovieResource {
    private String overview;

    public Movie toMovie() {
        try {
            return builder()
                    .overview(Optional.of(overview))
                    .build();
        } catch (NullPointerException | IllegalStateException e) {
            throw new InvalidInstantiationException(e);
        }
    }
}
