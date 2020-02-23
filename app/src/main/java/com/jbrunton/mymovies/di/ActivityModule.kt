package com.jbrunton.mymovies.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.jbrunton.mymovies.libs.ui.nav.NavigationController
import com.jbrunton.mymovies.nav.AppNavigationController
import org.koin.core.module.Module
import org.koin.core.qualifier.TypeQualifier

fun KoinActivityModule(activity: AppCompatActivity): Module {
    val qualifier = TypeQualifier(activity::class)
    return org.koin.dsl.module {
        scope(qualifier) {
            scoped(qualifier) { activity }
            scoped<NavigationController> {
                AppNavigationController(activity, get())
            }
        }
    }
}
