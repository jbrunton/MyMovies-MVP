package com.jbrunton.mymovies.ui.shared

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.di.ActivityModule
import com.jbrunton.mymovies.nav.Navigator
import com.jbrunton.mymovies.nav.ResultRouter
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), HasContainer,
        ActivityViewModelLifecycle
{
    val navigator: Navigator by inject()
    abstract val viewModel: T
    private val router: ResultRouter by inject()

    override val container by lazy {
        (applicationContext as HasContainer).container.createChildContainer().apply {
            register(ActivityModule(this@BaseActivity))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateLayout()
        onBindListeners()
        onObserveData()
        viewModel.start()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        router.onActivityResult(requestCode, resultCode, data)
    }

    override fun onCreateLayout() {}
    override fun onBindListeners() {}
    override fun onObserveData() {}

    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
