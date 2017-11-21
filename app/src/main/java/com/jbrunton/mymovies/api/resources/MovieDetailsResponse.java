package com.jbrunton.mymovies.api.resources;

import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

import java.util.Optional;

public class MovieDetailsResponse extends BaseMovieResource {
    private String overview;

    public static Movie toMovie(MovieDetailsResponse response, Configuration config) {
        return response.builder(config)
                .overview(Optional.of(response.overview))
                .build();
    }
}
