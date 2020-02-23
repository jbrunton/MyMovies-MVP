package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.TestGenresRepository
import com.jbrunton.mymovies.fixtures.repositories.TestMoviesRepository
import com.jbrunton.mymovies.networking.repositories.HttpAccountRepository
import com.jbrunton.mymovies.networking.services.ServiceFactory
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val TestHttpModule = Kodein.Module("TestHttpModule") {
    bind() from singleton { ServiceFactory.createService() }
    bind() from singleton { TestMoviesRepository() as MoviesRepository }
    bind() from singleton { TestGenresRepository() as GenresRepository }
    bind() from singleton { HttpAccountRepository(instance(), instance()) as AccountRepository }
}
