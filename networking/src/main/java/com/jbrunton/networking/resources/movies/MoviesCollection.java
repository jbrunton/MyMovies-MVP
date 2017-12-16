package com.jbrunton.networking.resources.movies;

import com.jbrunton.entities.Configuration;
import com.jbrunton.entities.Movie;
import com.jbrunton.networking.resources.PagedCollection;

import java.util.List;

public class MoviesCollection extends PagedCollection<MovieSearchResultResource> {
    public static List<Movie> toCollection(MoviesCollection response, Configuration config) {
        return response.toCollection(resource -> resource.toMovie(config));
    }
}
