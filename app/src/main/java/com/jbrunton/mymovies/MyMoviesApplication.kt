package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.mymovies.di.*

open class MyMoviesApplication : Application(), HasContainer {
    override val container = Container()

    inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        container.registerModules(schedulersModule(), httpModule(), uiModule())
    }

    open fun schedulersModule(): Module = SchedulersModule()
    open fun httpModule(): Module = HttpModule()
    open fun uiModule(): Module = UiModule()
}
