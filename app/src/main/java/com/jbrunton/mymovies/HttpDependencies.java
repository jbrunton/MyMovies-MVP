package com.jbrunton.mymovies;

import com.jbrunton.entities.GenresRepository;
import com.jbrunton.entities.MoviesRepository;
import com.jbrunton.networking.repositories.HttpGenresRepository;
import com.jbrunton.networking.repositories.HttpMoviesRepository;
import com.jbrunton.networking.services.MovieService;
import com.jbrunton.networking.services.ServiceFactory;

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
