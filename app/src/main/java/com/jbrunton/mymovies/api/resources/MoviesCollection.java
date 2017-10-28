package com.jbrunton.mymovies.api.resources;

import com.jbrunton.mymovies.models.InvalidInstantiationException;
import com.jbrunton.mymovies.models.Movie;

import java.util.List;

public class MoviesCollection extends PagedCollection<MovieSearchResultResource> {
    public List<Movie> toCollection() throws InvalidInstantiationException {
        return toCollection(MovieSearchResultResource::toMovie);
    }
}
