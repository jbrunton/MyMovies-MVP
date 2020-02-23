package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.repositories.AccountRepository
import com.jbrunton.mymovies.entities.repositories.GenresRepository
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.networking.repositories.HttpAccountRepository
import com.jbrunton.mymovies.networking.repositories.HttpGenresRepository
import com.jbrunton.mymovies.networking.repositories.HttpMoviesRepository
import com.jbrunton.mymovies.networking.services.ServiceFactory
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

val HttpModule = Module("HttpModule") {
    bind() from singleton { ServiceFactory.createService() }
    bind() from singleton { HttpMoviesRepository(instance(), instance()) as MoviesRepository }
    bind() from singleton { HttpGenresRepository(instance()) as GenresRepository }
    bind() from singleton { HttpAccountRepository(instance(), instance()) as AccountRepository }
}
