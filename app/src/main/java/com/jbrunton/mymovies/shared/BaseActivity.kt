package com.jbrunton.mymovies.shared

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.mymovies.nav.Navigator
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import org.koin.androidx.scope.ext.android.createScope
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.scope.Scope
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    var scope: Scope? = null
    val navigator: Navigator by inject()

    override fun onResume() {
        super.onResume()
        loadKoinModules(createActivityModule())
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
}
