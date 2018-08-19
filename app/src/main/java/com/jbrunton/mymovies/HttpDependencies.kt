package com.jbrunton.mymovies

import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.RxMovieService
import com.jbrunton.networking.services.MovieService
import com.jbrunton.networking.services.ServiceFactory

class HttpDependencies : ApplicationDependencies {
    val rxService: RxMovieService = ServiceFactory.createLegacyService()
    val service: MovieService = ServiceFactory.createService()

    override val moviesRepository: MoviesRepository = HttpMoviesRepository(rxService, service)
    override val genresRepository: GenresRepository = HttpGenresRepository(rxService)

    override val searchViewModelFactory: ViewModelProvider.Factory = SearchViewModel.Factory(moviesRepository)
}
