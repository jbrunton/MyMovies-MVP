package com.jbrunton.mymovies

import android.app.Application
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.mymovies.di.*

open class MyMoviesApplication : Application(), HasContainer {
    override val container = Container()

    inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        configureContainer(container)
    }

    open fun configureContainer(container: Container) {
        container.register(AppModule())
    }
}
