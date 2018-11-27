package com.jbrunton.mymovies.shared

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.account.AccountViewModel
import com.jbrunton.mymovies.main.MainViewModel
import com.jbrunton.mymovies.nav.Navigator
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlin.reflect.KClass

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), HasContainer {
    lateinit var navigator: Navigator

    override val container by lazy {
        (applicationContext as MyMoviesApplication).container.createChildContainer().apply {
            single { this@BaseActivity as AppCompatActivity }
            single { Navigator(get()) }
            factory { MainViewModel(get()) }
            factory { AccountViewModel(get(), get()) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        navigator = resolve()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        navigator.onActivityResult(requestCode, resultCode, data)
    }

    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    inline fun <reified T: ViewModel> resolveViewModel(noinline parameters: ParameterDefinition = emptyParameterDefinition()): T {
        return container.resolveViewModel(this, T::class, parameters)
    }
}
