package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.app.models.Movie;

import org.joda.time.LocalDate;

import java.util.Optional;

public class MovieResource {
    private String original_title;
    private String id;
    private String poster_path;
    private LocalDate release_date;
    private String vote_average;
    private String overview;

    public Movie toMovie() {
        return Movie.builder()
                .id(id)
                .title(original_title)
                .posterPath(poster_path)
                .releaseDate(Optional.ofNullable(release_date))
                .rating(vote_average)
                .overview(Optional.ofNullable(overview))
                .build();
    }
}
