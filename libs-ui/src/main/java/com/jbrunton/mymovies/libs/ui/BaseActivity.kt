package com.jbrunton.mymovies.libs.ui

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), HasContainer,
        ViewModelLifecycle, NavigationRequestListener
{
    val navigator: Navigator by inject()
    val navigationController: NavigationController by inject()
    abstract val viewModel: T

    override val container by lazy {
        (applicationContext as ActivityContainerFactory).createActivityContainer(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateLayout()
        onBindListeners()
        onObserveData()
        viewModel.start()
    }

    override fun onResume() {
        super.onResume()
        navigator.register(this)
    }

    override fun onPause() {
        super.onPause()
        navigator.unregister(this)
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
        navigationController.onActivityResult(requestCode, resultCode, data)
    }

    override fun onNavigationRequest(request: NavigationRequest) {
        navigationController.navigate(request)
    }

    override fun onCreateLayout() {}
    override fun onBindListeners() {}

    override fun onObserveData() {
        viewModel.snackbar.observe(this, this::showSnackbar)
    }

    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    private fun showSnackbar(event: SnackbarEvent) {
        val snackbar = Snackbar.make(findViewById(android.R.id.content), event.message, event.duration)
        if (event.actionLabel != null) {
            snackbar.setAction(event.actionLabel, { event.action?.invoke() })
        }
        snackbar.show()
    }
}
