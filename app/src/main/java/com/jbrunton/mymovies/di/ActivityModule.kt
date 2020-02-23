package com.jbrunton.mymovies.di

import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.libs.ui.ActivityModuleFactory
import com.jbrunton.mymovies.libs.ui.nav.NavigationController
import com.jbrunton.mymovies.nav.AppNavigationController
import org.koin.core.module.Module
import org.koin.core.qualifier.TypeQualifier
import org.koin.dsl.module

fun ActivityModule(activity: AppCompatActivity): Module {
    val qualifier = TypeQualifier(activity::class)
    return module {
        scope(qualifier) {
            scoped(qualifier) { activity }
            scoped<NavigationController> {
                AppNavigationController(activity, get())
            }
        }
    }
}
