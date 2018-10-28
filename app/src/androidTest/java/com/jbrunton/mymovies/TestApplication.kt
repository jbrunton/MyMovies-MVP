package com.jbrunton.mymovies

import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.discover.DiscoverViewModel
import com.jbrunton.mymovies.discover.GenreResultsViewModel
import com.jbrunton.mymovies.discover.GenresViewModel
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.networking.services.ServiceFactory
import com.nhaarman.mockito_kotlin.mock
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val testModule : Module = module {
    single { mock<ServiceFactory>() }
    single { mock<MoviesRepository>() }
    single { mock<GenresRepository>() }

    viewModel { SearchViewModel(get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
    viewModel { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
}

class TestApplication : MyMoviesApplication() {
    override fun createDependencies() = listOf(testModule)
}
