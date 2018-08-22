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
import org.koin.android.architecture.ext.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val testModule : Module = applicationContext {
    bean { mock<ServiceFactory>() }
    bean { mock<MoviesRepository>() }
    bean { mock<GenresRepository>() }

    viewModel {
        object : SearchViewModel(get()) {
            override fun performSearch(query: String) {}
        } as SearchViewModel
    }

    viewModel { DiscoverViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { params -> MovieDetailsViewModel(params["MOVIE_ID"], get()) }
    viewModel { params -> GenreResultsViewModel(params["GENRE_ID"], get()) }
}

class TestApplication : MyMoviesApplication() {
    override fun createDependencies() = listOf(testModule)
}
