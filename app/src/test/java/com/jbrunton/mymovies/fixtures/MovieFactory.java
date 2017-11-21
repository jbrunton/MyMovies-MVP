package com.jbrunton.mymovies.fixtures;

import com.jbrunton.entities.Movie;

import java.util.Optional;

public class MovieFactory {
    private int count = 0;

    public Movie.Builder builder() {
        final String id = Integer.toString(++count);
        return Movie.builder()
                .id(id)
                .title("Movie " + id)
                .overview(Optional.of("Overview for Movie " + id))
                .releaseDate(Optional.empty())
                .posterUrl(Optional.empty())
                .backdropUrl(Optional.empty())
                .rating("");
    }

    public Movie create() {
        return builder().build();
    }
}
