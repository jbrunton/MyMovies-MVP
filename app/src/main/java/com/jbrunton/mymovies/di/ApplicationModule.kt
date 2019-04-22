package com.jbrunton.mymovies.di

import android.content.Context
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.inject.module
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.SharedApplicationPreferences

fun ApplicationModule(application: MyMoviesApplication) = module {
    single { application as Context }
    single { SharedApplicationPreferences(get()) as ApplicationPreferences }
}