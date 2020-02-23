package com.jbrunton.mymovies

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.libs.ui.ActivityContainerFactory
import com.jbrunton.mymovies.di.ActivityModule
import com.jbrunton.mymovies.di.ApplicationComponent
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.subKodein

open class MyMoviesApplication : Application(), KodeinAware, ActivityContainerFactory {
    override val kodein by Kodein.lazy {
        importAll(createApplicationComponent().createModules())
    }

    open fun createApplicationComponent() = ApplicationComponent(this)

    override fun createActivityContainer(activity: AppCompatActivity): Kodein {
        return Kodein {
            extend(kodein)
            import(ActivityModule(activity))
        }
    }

//    override fun createActivityModule(activity: AppCompatActivity): Container {
//        return container.createChildContainer().apply {
//            register(ActivityModule(activity))
//        }
//    }
}
