package com.jbrunton.mymovies.api.resources;

import com.google.common.base.Optional;
import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

public class MovieDetailsResponse extends BaseMovieResource {
    private String overview;

    public static Movie toMovie(MovieDetailsResponse response, Configuration config) {
        return response.builder(config)
                .overview(Optional.of(response.overview))
                .build();
    }
}
