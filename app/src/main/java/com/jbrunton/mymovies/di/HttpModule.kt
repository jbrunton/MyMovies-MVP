package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.inject.module
import com.jbrunton.mymovies.networking.repositories.HttpAccountRepository
import com.jbrunton.mymovies.networking.repositories.HttpGenresRepository
import com.jbrunton.mymovies.networking.repositories.HttpMoviesRepository
import com.jbrunton.mymovies.networking.services.ServiceFactory

val HttpModule = module {
    single { ServiceFactory.createService() }
    single { HttpMoviesRepository(get(), get()) as MoviesRepository }
    single { HttpGenresRepository(get()) as GenresRepository }
    single { HttpAccountRepository(get(), get()) as AccountRepository }
}

val KoinHttpModule = org.koin.dsl.module {
    single { ServiceFactory.createService() }
    single { HttpMoviesRepository(get(), get()) as MoviesRepository }
    single { HttpGenresRepository(get()) as GenresRepository }
    single { HttpAccountRepository(get(), get()) as AccountRepository }
}
