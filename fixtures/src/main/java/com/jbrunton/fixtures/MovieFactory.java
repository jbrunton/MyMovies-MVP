package com.jbrunton.fixtures;

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
                .posterUrl(Optional.of("https://image.tmdb.org/t/p/w300/bIuOWTtyFPjsFDevqvF3QrD1aun.jpg"))
                .backdropUrl(Optional.of("https://image.tmdb.org/t/p/w300/LvmmDZxkTDqp0DX7mUo621ahdX.jpg"))
                .rating("");
    }

    public Movie create() {
        return builder().build();
    }
}
