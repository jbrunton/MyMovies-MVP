package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.TestGenresRepository
import com.jbrunton.mymovies.fixtures.repositories.TestMoviesRepository
import com.jbrunton.mymovies.networking.repositories.HttpAccountRepository
import com.jbrunton.mymovies.networking.services.ServiceFactory
import org.koin.dsl.module

val TestHttpModule = module {
    single { ServiceFactory.createService() }
    single { TestMoviesRepository() as MoviesRepository }
    single { TestGenresRepository() as GenresRepository }
    single { HttpAccountRepository(get(), get()) as AccountRepository }
}
