package com.jbrunton.mymovies.api.resources;

import com.google.common.base.Optional;
import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

public class MovieSearchResultResource extends BaseMovieResource {
    public Movie toMovie(Configuration config) {
        return builder(config)
                .overview(Optional.absent())
                .build();
    }
}
