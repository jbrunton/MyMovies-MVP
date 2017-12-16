package com.jbrunton.mymovies;

import com.jbrunton.entities.GenresRepository;
import com.jbrunton.entities.MoviesRepository;

public interface ApplicationDependencies {
    MoviesRepository moviesRepository();
    GenresRepository genresRepository();
}
