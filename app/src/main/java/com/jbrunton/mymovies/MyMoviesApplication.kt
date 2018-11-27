package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.account.AccountViewModel
import com.jbrunton.mymovies.auth.LoginViewModel
import com.jbrunton.mymovies.discover.DiscoverViewModel
import com.jbrunton.mymovies.discover.GenreResultsViewModel
import com.jbrunton.mymovies.discover.GenresViewModel
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.HasContainer
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
        registerDependencies(container)
    }

    protected open fun registerDependencies(container: Container) {
        container.apply {
            single { ServiceFactory.createService() }
            single { HttpMoviesRepository(get()) as MoviesRepository }
            single { HttpGenresRepository(get()) as GenresRepository }
            single { HttpAccountRepository(get()) as AccountRepository }
            single { Schedulers.computation() }

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
