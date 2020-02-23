package com.jbrunton.mymovies.di

import android.content.Context
import com.jbrunton.mymovies.entities.repositories.ApplicationPreferences
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.SharedApplicationPreferences
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun ApplicationModule(application: MyMoviesApplication) = Kodein.Module("ApplicationModule") {
    bind() from singleton { application as Context }
    bind() from singleton { SharedApplicationPreferences(instance()) as ApplicationPreferences }
}
