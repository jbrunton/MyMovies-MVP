package com.jbrunton.mymovies.libs.ui.views

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import org.kodein.di.*
import org.kodein.di.android.subKodein
import org.kodein.di.generic.instance
import kotlin.reflect.KClass

class KodeinViewModelFactory(val kodein: Kodein) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
            kodein.direct.Instance(TT(modelClass))
}

fun <T : FragmentActivity, VM: ViewModel> T.resolveViewModel(
        klass: KClass<VM>
): VM where T : KodeinAware {
    return ViewModelProviders.of(this, KodeinViewModelFactory(kodein)).get(klass.java)
}

fun <T : Fragment, VM: ViewModel> T.resolveViewModel(
        klass: KClass<VM>
): VM where T : KodeinAware {
    return ViewModelProviders.of(this, KodeinViewModelFactory(kodein)).get(klass.java)
}

inline fun <T : FragmentActivity, reified VM: ViewModel> T.injectViewModel(

): Lazy<VM> where T: KodeinAware = lazy { resolveViewModel(VM::class) }

inline fun <T : Fragment, reified VM: ViewModel> T.injectViewModel(

): Lazy<VM> where T: KodeinAware = lazy { resolveViewModel(VM::class) }

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), KodeinAware,
        ViewModelLifecycle, NavigationRequestListener
{
    val navigator: Navigator by instance()
    val navigationController: NavigationController by instance()
    abstract val viewModel: T

    override val kodein by lazy {
        (applicationContext as ActivityContainerFactory)
                .createActivityContainer(this@BaseActivity)
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
