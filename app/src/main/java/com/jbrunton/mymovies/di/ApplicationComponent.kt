package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.MyMoviesApplication
import org.koin.core.module.Module

open class ApplicationComponent(val application: MyMoviesApplication) {
    open fun createModules(): List<Module> {
        return listOf(
                appModule(),
                schedulersModule(),
                httpModule(),
                uiModule()
        )
    }

    open fun appModule(): Module = KoinApplicationModule(application)
    open fun schedulersModule(): Module = KoinSchedulersModule
    open fun httpModule(): Module = KoinHttpModule
    open fun uiModule(): Module = KoinUiModule
}
