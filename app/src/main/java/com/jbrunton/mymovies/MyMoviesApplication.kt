package com.jbrunton.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.di.*
import com.jbrunton.mymovies.libs.ui.ActivityModuleFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.koinApplication

open class MyMoviesApplication : Application(), ActivityModuleFactory, KoinComponent {
    //override val container = Container()

    //inline fun <reified T : Any> get() = container.get<T>()

    private lateinit var koin: Koin

    override fun getKoin(): Koin {
        return koin
    }

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
        koin = koinApplication {
            androidContext(this@MyMoviesApplication)
            modules(applicationModules)
        }.koin
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
