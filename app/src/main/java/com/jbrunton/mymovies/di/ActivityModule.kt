package com.jbrunton.mymovies.di

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.jbrunton.inject.module
import com.jbrunton.mymovies.libs.ui.nav.NavigationController
import com.jbrunton.mymovies.libs.ui.views.BaseActivity
import com.jbrunton.mymovies.nav.AppNavigationController
import com.jbrunton.mymovies.ui.main.MainActivity
import org.koin.core.module.Module
import org.koin.core.qualifier.TypeQualifier
import org.koin.core.qualifier.named

fun ActivityModule(activity: FragmentActivity) = module {
    single { activity }
    single { AppNavigationController(get(), get()) as NavigationController }
}

//fun KoinActivityModule(activity: MainActivity) = org.koin.dsl.module {
//    scope(named<MainActivity>()) {
//        scoped<MainActivity> { activity }
//        scoped<NavigationController> {
//            AppNavigationController(activity, get())
//        }
//    }
//}

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
