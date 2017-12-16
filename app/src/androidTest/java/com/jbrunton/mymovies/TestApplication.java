package com.jbrunton.mymovies;

import com.jbrunton.entities.GenresRepository;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.mymovies.app.ApplicationDependencies;

public class TestApplication extends MyMoviesApplication {
    @Override protected ApplicationDependencies createDependencyGraph() {
        return new ApplicationDependencies() {
            @Override public MoviesRepository moviesRepository() {
                throw new RuntimeException("Stub!");
            }

            @Override public GenresRepository genresRepository() {
                throw new RuntimeException("Stub!");
            }
        };
    }
}
