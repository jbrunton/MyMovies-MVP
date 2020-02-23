package com.jbrunton.mymovies.di

import android.content.Context
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.SharedApplicationPreferences
import com.jbrunton.mymovies.libs.ui.ActivityModuleFactory


fun ApplicationModule(application: MyMoviesApplication) = org.koin.dsl.module {
    single { application.getKoin() }
    //single { application as Context }
    single { SharedApplicationPreferences(get()) as ApplicationPreferences }
    single { application as ActivityModuleFactory }
}
