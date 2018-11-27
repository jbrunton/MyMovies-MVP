package com.jbrunton.mymovies.shared

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.nav.Navigator
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.emptyParameterDefinition
import org.koin.core.scope.Scope
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules
import kotlin.reflect.KClass

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), HasContainer {
    init {
        loadKoinModules(createActivityModule())
    }

    var scope: Scope? = null
    lateinit var navigator: Navigator

    override val container by lazy {
        (applicationContext as MyMoviesApplication).container.createChildContainer().apply {
            single { this@BaseActivity as AppCompatActivity }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = resolve()
    }

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

    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return container.resolveViewModel(this, T::class, parameters)
    }
}
