package com.jbrunton.mymovies

import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.MovieService
import com.jbrunton.networking.services.ServiceFactory

class HttpDependencies : ApplicationDependencies {
    val service: MovieService = ServiceFactory.createLegacyService()

    override val moviesRepository: MoviesRepository = HttpMoviesRepository(service)
    override val genresRepository: GenresRepository = HttpGenresRepository(service)

    override val searchViewModelFactory: ViewModelProvider.Factory = SearchViewModel.Factory(moviesRepository)
}
