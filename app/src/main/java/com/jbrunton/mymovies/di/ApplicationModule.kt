package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.SharedApplicationPreferences


fun KoinApplicationModule(application: MyMoviesApplication) = org.koin.dsl.module {
    //single { application as Context }
    single { SharedApplicationPreferences(get()) as ApplicationPreferences }
}
