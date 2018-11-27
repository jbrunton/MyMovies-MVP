package com.jbrunton.mymovies

import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.di.Container
import com.jbrunton.mymovies.di.Module
import com.jbrunton.mymovies.fixtures.TestMoviesRepository
import com.jbrunton.networking.repositories.HttpAccountRepository
import com.jbrunton.networking.services.ServiceFactory
import io.reactivex.schedulers.Schedulers

class TestApplication : MyMoviesApplication() {
    override fun schedulersModule() = object : Module {
        override fun registerTypes(container: Container) {
            container.apply {
                single(override = true) { Schedulers.trampoline() }
            }
        }
    }

    override fun httpModule() = object : Module {
        override fun registerTypes(container: Container) {
            container.apply {
                single(override = true) { ServiceFactory.createService() }
                single(override = true) { TestMoviesRepository() as MoviesRepository }
                single(override = true) { TestGenresRepository() as GenresRepository }
                single(override = true) { HttpAccountRepository(get()) as AccountRepository }
            }
        }
    }
}
