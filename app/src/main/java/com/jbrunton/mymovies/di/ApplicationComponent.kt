package com.jbrunton.mymovies.di

import com.jbrunton.mymovies.MyMoviesApplication
import org.kodein.di.Kodein.Module

open class ApplicationComponent(val application: MyMoviesApplication) {
    open fun createModules(): List<Module> {
        return listOf(
                appModule(),
                schedulersModule(),
                httpModule(),
                uiModule()
        )
    }

    open fun appModule(): Module = ApplicationModule(application)
    open fun schedulersModule(): Module = SchedulersModule
    open fun httpModule(): Module = HttpModule
    open fun uiModule(): Module = UiModule
}