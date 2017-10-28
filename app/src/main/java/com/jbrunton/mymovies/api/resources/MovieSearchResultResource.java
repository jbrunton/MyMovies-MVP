package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.InvalidInstantiationException;
import com.jbrunton.mymovies.models.Movie;

import java.util.Optional;

public class MovieSearchResultResource extends BaseMovieResource {
    public Movie toMovie() {
        try {
            return builder()
                    .overview(Optional.empty())
                    .build();
        } catch (NullPointerException | IllegalStateException e) {
            throw new InvalidInstantiationException(e);
        }
    }
}
