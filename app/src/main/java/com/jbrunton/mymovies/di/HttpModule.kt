package com.jbrunton.mymovies.di

import com.jbrunton.entities.repositories.AccountRepository
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.networking.repositories.HttpAccountRepository
import com.jbrunton.networking.repositories.HttpGenresRepository
import com.jbrunton.networking.repositories.HttpMoviesRepository
import com.jbrunton.networking.services.ServiceFactory

class HttpModule : Module {
    override fun registerTypes(container: Container) {
        container.apply {
            single { ServiceFactory.createService() }
            single { HttpMoviesRepository(get()) as MoviesRepository }
            single { HttpGenresRepository(get()) as GenresRepository }
            single { HttpAccountRepository(get()) as AccountRepository }
        }
    }
}
