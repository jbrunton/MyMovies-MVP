package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.app.models.Movie;

import java.util.List;

public class MoviesCollection extends PagedCollection<MovieSearchResultResource> {
    public List<Movie> toCollection() {
        return toCollection(MovieSearchResultResource::toMovie);
    }
}
