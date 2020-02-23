package com.jbrunton.mymovies.di

import androidx.fragment.app.FragmentActivity
import com.jbrunton.mymovies.libs.ui.nav.NavigationController
import com.jbrunton.mymovies.nav.AppNavigationController
import org.kodein.di.Kodein
import org.kodein.di.Kodein.Module
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton

fun ActivityModule(activity: FragmentActivity) = Module("ActivityModule") {
    bind() from singleton { activity }
    bind() from singleton { AppNavigationController(instance(), instance()) as NavigationController }
}
