package com.jbrunton.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.mymovies.di.*
import com.jbrunton.mymovies.libs.ui.ActivityContainerFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.module.Module

open class MyMoviesApplication : Application() {
    //override val container = Container()

    //inline fun <reified T : Any> get() = container.get<T>()

    override fun onCreate() {
        super.onCreate()
        //configureContainer(container)
        startKoin {
            androidContext(this@MyMoviesApplication)
            modules(createModules())
        }
    }

    open fun createModules(): List<Module> {
        return listOf(
                KoinApplicationModule(this),
                KoinSchedulersModule,
                KoinHttpModule,
                KoinUiModule)
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
