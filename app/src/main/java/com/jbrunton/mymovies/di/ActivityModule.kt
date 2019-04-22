package com.jbrunton.mymovies.di

import androidx.fragment.app.FragmentActivity
import com.jbrunton.inject.module
import com.jbrunton.mymovies.libs.ui.NavigationController
import com.jbrunton.mymovies.nav.AppNavigationController

fun ActivityModule(activity: FragmentActivity) = module {
    single { activity }
    single { AppNavigationController(get(), get()) as NavigationController }
}
