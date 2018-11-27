package com.jbrunton.mymovies.shared

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.nav.Navigator
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.scope.Scope
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import kotlin.reflect.KClass

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    init {
        loadKoinModules(createActivityModule())
    }

    var scope: Scope? = null
    val navigator: Navigator by inject()

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        navigator.onActivityResult(requestCode, resultCode, data)
    }

    override fun onPause() {
        super.onPause()
        scope?.close()
    }

    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun createActivityModule(): Module {
        scope = getOrCreateScope("ACTIVITY")
        return module(override = true) {
            scope("ACTIVITY") { this@BaseActivity as AppCompatActivity }
        }
    }

    fun <T : Any> resolve(klass: KClass<T>): T {
        return (applicationContext as MyMoviesApplication).container.resolve(klass)
    }

    inline fun <reified T: ViewModel> resolve(): T {
        return resolve(T::class)
    }

    inline fun <reified T: ViewModel> resolveViewModel(): T {
        return (applicationContext as MyMoviesApplication).container.viewModel(this, T::class)
    }
}
