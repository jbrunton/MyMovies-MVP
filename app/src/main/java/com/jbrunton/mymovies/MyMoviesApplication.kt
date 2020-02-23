package com.jbrunton.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.di.*
import com.jbrunton.mymovies.libs.ui.ActivityModuleFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class MyMoviesApplication : Application(), ActivityModuleFactory {
    //override val container = Container()

    //inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        configureDependencies()
    }

    override fun createActivityModule(activity: AppCompatActivity): Module {
        return ActivityModule(activity)
    }

    protected open fun createApplicationComponent() = ApplicationComponent(this)

    private fun configureDependencies() {
        val applicationModules = createApplicationComponent().createModules()
        startKoin {
            androidContext(this@MyMoviesApplication)
            modules(applicationModules)
        }
    }

//    open fun configureContainer(container: Container) {
//        container.register(ApplicationComponent(this))
//    }
//
//    override fun createActivityContainer(activity: AppCompatActivity): Container {
//        return container.createChildContainer().apply {
//            register(ActivityModule(activity))
//        }
//    }
}
