package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.Configuration;
import com.jbrunton.mymovies.models.Movie;

import java.util.Optional;

public class MovieSearchResultResource extends BaseMovieResource {
    public Movie toMovie(Configuration config) {
        return builder(config)
                .overview(Optional.empty())
                .build();
    }
}
