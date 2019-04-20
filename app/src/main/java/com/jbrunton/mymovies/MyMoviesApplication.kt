package com.jbrunton.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.libs.ui.ActivityContainerFactory
import com.jbrunton.mymovies.di.ActivityModule
import com.jbrunton.mymovies.di.ApplicationComponent

open class MyMoviesApplication : Application(), HasContainer, ActivityContainerFactory {
    override val container = Container()

    inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        configureContainer(container)
    }

    open fun configureContainer(container: Container) {
        container.register(ApplicationComponent(this))
    }

    override fun createActivityContainer(activity: AppCompatActivity): Container {
        return container.createChildContainer().apply {
            register(ActivityModule(activity))
        }
    }
}
