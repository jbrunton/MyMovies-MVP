package com.jbrunton.mymovies

import android.app.Application

open class MyMoviesApplication : Application() {
    lateinit var dependencies: ApplicationDependencies private set

    override fun onCreate() {
        super.onCreate()
        dependencies = createDependencyGraph()
    }

    protected open fun createDependencyGraph(): ApplicationDependencies {
        return HttpDependencies()
    }
}
