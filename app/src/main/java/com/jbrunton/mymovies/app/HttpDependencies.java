package com.jbrunton.mymovies.app;

import com.jbrunton.entities.GenresRepository;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.mymovies.api.repositories.HttpGenresRepository;
import com.jbrunton.mymovies.api.repositories.HttpMoviesRepository;
import com.jbrunton.mymovies.api.services.MovieService;
import com.jbrunton.mymovies.api.services.ServiceFactory;

public class HttpDependencies implements ApplicationDependencies {
    private MovieService service;

    private MoviesRepository moviesRepository;
    private GenresRepository genresRepository;

    @Override public MoviesRepository moviesRepository() {
        if (moviesRepository == null) {
            return new HttpMoviesRepository(movieService());
        }
        return moviesRepository;
    }

    @Override public GenresRepository genresRepository() {
        if (genresRepository == null) {
            return new HttpGenresRepository(movieService());
        }
        return genresRepository;
    }

    protected MovieService movieService() {
        if (service == null) {
            service = ServiceFactory.createService();
        }
        return service;
    }
}
