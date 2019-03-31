package com.jbrunton.mymovies.di

import androidx.fragment.app.FragmentActivity
import com.jbrunton.inject.module
import com.jbrunton.mymovies.nav.NavigationController

fun ActivityModule(activity: FragmentActivity) = module {
    single { activity }
    single { NavigationController(get(), get()) }
}
