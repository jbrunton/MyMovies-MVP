package com.jbrunton.mymovies;

import com.jbrunton.entities.GenresRepository;
import com.jbrunton.entities.MoviesRepository;

import static org.mockito.Mockito.mock;

public class TestApplication extends MyMoviesApplication {
    @Override protected ApplicationDependencies createDependencyGraph() {
        return new ApplicationDependencies() {
            @Override public MoviesRepository moviesRepository() {
                return mock(MoviesRepository.class);
            }

            @Override public GenresRepository genresRepository() {
                return mock(GenresRepository.class);
            }
        };
    }
}
