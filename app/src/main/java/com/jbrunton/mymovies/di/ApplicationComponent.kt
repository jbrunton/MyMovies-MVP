package com.jbrunton.mymovies.di

import com.jbrunton.inject.Container
import com.jbrunton.inject.Module
import com.jbrunton.mymovies.MyMoviesApplication

open class ApplicationComponent(val application: MyMoviesApplication) : Module {
    override fun registerTypes(container: Container) {
        container.single { container }
        container.register(
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