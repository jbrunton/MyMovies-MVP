package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.entities.GenresRepository
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.discover.DiscoverViewModel
import com.jbrunton.mymovies.discover.GenreResultsViewModel
import com.jbrunton.mymovies.discover.GenresViewModel
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.mymovies.shared.CoroutineDispatchers
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.ServiceFactory
import kotlinx.coroutines.CommonPool
import kotlinx.coroutines.android.UI
import org.koin.android.ext.android.startKoin
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.applicationContext

val applicationModule : Module = applicationContext {
    single { ServiceFactory.createService() }
    single { HttpMoviesRepository(get()) as MoviesRepository }
    single { HttpGenresRepository(get()) as GenresRepository }
    single { CoroutineDispatchers(UI, CommonPool) }
    viewModel { SearchViewModel(get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { (movieId: String) -> MovieDetailsViewModel(movieId, get(), get()) }
    viewModel { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
}

open class MyMoviesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, createDependencies())
    }

    protected open fun createDependencies() = listOf(applicationModule)
}
