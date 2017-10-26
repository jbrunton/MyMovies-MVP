package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.Movie;

import java.util.List;

public class MoviesCollection extends PagedCollection<MovieResource> {
    public List<Movie> toCollection() {
        return toCollection(MovieResource::toMovie);
    }
}
