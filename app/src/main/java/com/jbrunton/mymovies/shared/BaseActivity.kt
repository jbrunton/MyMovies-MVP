package com.jbrunton.mymovies.shared

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.androidx.scope.ext.android.getOrCreateScope
import org.koin.core.scope.Scope
import org.koin.dsl.module.Module
import org.koin.dsl.module.module
import org.koin.standalone.StandAloneContext.loadKoinModules

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    private lateinit var activityScope: Scope

    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadKoinModules(createActivityModule())
    }

    override fun onDestroy() {
        super.onDestroy()
        activityScope.close()
    }

    fun createActivityModule(): Module {
        activityScope = getOrCreateScope("ACTIVITY")
        return module {
            scope("ACTIVITY") { this@BaseActivity as Activity }
        }
    }
}
