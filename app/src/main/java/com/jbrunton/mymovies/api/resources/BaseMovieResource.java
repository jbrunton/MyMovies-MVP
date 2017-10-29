package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.app.models.Movie;

import org.joda.time.LocalDate;

import java.util.Optional;

public class BaseMovieResource {
    private String id;
    private String originalTitle;
    private String posterPath;
    private LocalDate releaseDate;
    private String voteAverage;

    protected Movie.Builder builder() {
        return Movie.builder()
                .id(id)
                .title(originalTitle)
                .posterPath(Optional.ofNullable(posterPath))
                .releaseDate(Optional.ofNullable(releaseDate))
                .rating(voteAverage);
    }
}
