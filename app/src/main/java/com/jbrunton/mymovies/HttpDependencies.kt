package com.jbrunton.mymovies

import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.MovieService
import com.jbrunton.networking.services.ServiceFactory

class HttpDependencies : ApplicationDependencies {
    val service: MovieService by lazy { ServiceFactory.createService() }

    override val moviesRepository: MoviesRepository by lazy { HttpMoviesRepository(service) }
    override val genresRepository: GenresRepository by lazy { HttpGenresRepository(service) }
}
