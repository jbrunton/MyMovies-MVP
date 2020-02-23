package com.jbrunton.mymovies.libs.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.mymovies.libs.ui.*
import com.jbrunton.mymovies.libs.ui.livedata.observe
import com.jbrunton.mymovies.libs.ui.nav.NavigationController
import com.jbrunton.mymovies.libs.ui.nav.NavigationRequest
import com.jbrunton.mymovies.libs.ui.nav.NavigationRequestListener
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.libs.ui.viewmodels.ViewModelLifecycle
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.get
import org.koin.androidx.scope.lifecycleScope
import org.koin.core.Koin
import org.koin.core.KoinComponent
import org.koin.core.module.Module


abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(),
        ViewModelLifecycle, NavigationRequestListener, KoinComponent
{
    val navigator by lazy { lifecycleScope.get<Navigator>() }
    val navigationController by lazy { lifecycleScope.get<NavigationController>() }
    abstract val viewModel: T

    override fun getKoin(): Koin = (application as KoinComponent).getKoin()

    private lateinit var activityModule: Module

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityModule = get<ActivityModuleFactory>().createActivityModule(this)
        getKoin().loadModules(listOf(activityModule))

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

    override fun onDestroy() {
        super.onDestroy()
        getKoin().unloadModules(listOf(activityModule))
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
