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
import com.jbrunton.mymovies.main.MainViewModel
import com.jbrunton.mymovies.moviedetails.MovieDetailsViewModel
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.search.SearchViewModel
import com.jbrunton.mymovies.shared.Container
import com.jbrunton.networking.repositories.HttpAccountRepository
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.ServiceFactory
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

val applicationModule : Module = module {
    single { ServiceFactory.createService() }
    single { HttpMoviesRepository(get()) as MoviesRepository }
    single { HttpGenresRepository(get()) as GenresRepository }
    single { HttpAccountRepository(get()) as AccountRepository }
    single { Schedulers.computation() }
    single { Navigator() }
    viewModel { MainViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { DiscoverViewModel(get()) }
    viewModel { GenresViewModel(get()) }
    viewModel { AccountViewModel(get(), get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { (movieId: String) -> MovieDetailsViewModel(movieId, get()) }
    viewModel { (genreId: String) -> GenreResultsViewModel(genreId, get()) }
}

open class MyMoviesApplication : Application() {
    val container = Container()

    inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        startKoin(this, createDependencies())
        registerDependencies(container)
    }

    protected open fun registerDependencies(container: Container) {
        container.single { ServiceFactory.createService() }
        container.single { HttpMoviesRepository(get()) as MoviesRepository }
        container.single { HttpGenresRepository(get()) as GenresRepository }
        container.single { HttpAccountRepository(get()) as AccountRepository }
        container.single { Schedulers.computation() }
        container.single { Navigator() }

        container.factory { MainViewModel(get()) }
    }

    protected open fun createDependencies() = listOf(applicationModule)
}
