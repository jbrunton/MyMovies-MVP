package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.discover.DiscoverViewModel
import com.jbrunton.mymovies.discover.GenreResultsViewModel
import com.jbrunton.mymovies.discover.GenresViewModel
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.ServiceFactory
import org.koin.android.architecture.ext.viewModel
import org.koin.android.ext.android.startKoin
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val applicationModule : Module = applicationContext {
    bean { ServiceFactory.createService() }
    bean { HttpMoviesRepository(get()) as MoviesRepository }
    bean { HttpGenresRepository(get()) as GenresRepository }
    viewModel { SearchViewModel(get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { params -> MovieDetailsViewModel(params["MOVIE_ID"], get()) }
    viewModel { params -> GenreResultsViewModel(params["GENRE_ID"], get()) }
}

open class MyMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, createDependencies())
    }

    protected open fun createDependencies() = listOf(applicationModule)
}
