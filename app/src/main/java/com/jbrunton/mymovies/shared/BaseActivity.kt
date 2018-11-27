package com.jbrunton.mymovies.shared

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.account.AccountViewModel
import com.jbrunton.mymovies.di.*
import com.jbrunton.mymovies.nav.Navigator
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), HasContainer {
    val navigator: Navigator by inject()

    override val container by lazy {
        (applicationContext as HasContainer).container.createChildContainer().apply {
            registerModules(ApplicationModule(this@BaseActivity))
        }
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

    class ApplicationModule(val activity: FragmentActivity): Module {
        override fun registerTypes(container: Container) {
            container.apply {
                single { activity }
                single { Navigator(get()) }
            }
        }
    }
}
