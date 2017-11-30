package com.jbrunton.mymovies.api.resources;

import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

import java.util.Optional;

public class MovieSearchResultResource extends BaseMovieResource {
    public Movie toMovie(Configuration config) {
        return builder(config)
                .overview(Optional.empty())
                .build();
    }
}
