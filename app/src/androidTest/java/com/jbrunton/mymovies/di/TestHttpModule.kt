package com.jbrunton.mymovies.di

import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.inject.module
import com.jbrunton.mymovies.TestGenresRepository
import com.jbrunton.mymovies.fixtures.repositories.TestMoviesRepository
import com.jbrunton.networking.repositories.HttpAccountRepository
import com.jbrunton.networking.services.ServiceFactory

val TestHttpModule = module {
    single { ServiceFactory.createService() }
    single { TestMoviesRepository() as MoviesRepository }
    single { TestGenresRepository() as GenresRepository }
    single { HttpAccountRepository(get(), get()) as AccountRepository }
}
