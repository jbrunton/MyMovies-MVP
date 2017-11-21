package com.jbrunton.mymovies.api.resources;

import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;

import org.joda.time.LocalDate;

import java.util.Optional;

public class BaseMovieResource {
    private String id;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private LocalDate releaseDate;
    private String voteAverage;

    protected Movie.Builder builder(Configuration config) {
        return Movie.builder()
                .id(id)
                .title(originalTitle)
                .posterUrl(config.expandUrl(posterPath))
                .backdropUrl(config.expandUrl(backdropPath))
                .releaseDate(Optional.ofNullable(releaseDate))
                .rating(voteAverage);
    }
}
