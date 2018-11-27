package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.ui.account.AccountViewModel
import com.jbrunton.mymovies.ui.auth.LoginViewModel
import com.jbrunton.mymovies.ui.discover.DiscoverViewModel
import com.jbrunton.mymovies.ui.discover.GenreResultsViewModel
import com.jbrunton.mymovies.ui.discover.GenresViewModel
import com.jbrunton.mymovies.ui.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.ui.search.SearchViewModel
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.HasContainer
import com.jbrunton.mymovies.di.Module
import com.jbrunton.networking.repositories.HttpAccountRepository
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.ServiceFactory
import io.reactivex.schedulers.Schedulers

open class MyMoviesApplication : Application(), HasContainer {
    override val container = Container()

    inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        container.registerModules(schedulersModule(), httpModule(), viewModelsModule())
    }

    open fun schedulersModule() = object : Module {
        override fun registerTypes(container: Container) {
            container.apply {
                single { Schedulers.computation() }
            }
        }
    }

    open fun httpModule() = object : Module {
        override fun registerTypes(container: Container) {
            container.apply {
                single { ServiceFactory.createService() }
                single { HttpMoviesRepository(get()) as MoviesRepository }
                single { HttpGenresRepository(get()) as GenresRepository }
                single { HttpAccountRepository(get()) as AccountRepository }
            }
        }
    }

    open fun viewModelsModule() = object : Module {
        override fun registerTypes(container: Container) {
            container.apply {
                factory { SearchViewModel(get()) }
                factory { DiscoverViewModel(get()) }
                factory { GenresViewModel(get()) }
                factory { LoginViewModel(get()) }
                factory { AccountViewModel(get()) }
                factory { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
                factory { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
            }
        }
    }
}
