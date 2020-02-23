package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.HasSchedulers
import com.jbrunton.mymovies.entities.SchedulerContext
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.libs.ui.SnackbarEvent
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.NavigationResult
import com.jbrunton.mymovies.libs.ui.nav.NavigationResultListener
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

abstract class BaseViewModel(override val kodein: Kodein) : ViewModel(),
        HasSchedulers, KodeinAware, NavigationResultListener
{
    val navigator: Navigator by instance()
    val schedulerFactory: SchedulerFactory by instance()
    override val schedulerContext = SchedulerContext(schedulerFactory)
    val snackbar = SingleLiveEvent<SnackbarEvent>()

    open fun start() {
        navigator.register(this)
    }

    fun<T>failure(errorMessage: String,
                  @DrawableRes errorIcon: Int = 0,
                  allowRetry: Boolean = false
    ): AsyncResult<T> {
        return AsyncResult.Failure(LoadingViewStateError(errorMessage, errorIcon, allowRetry))
    }

    override fun onNavigationResult(result: NavigationResult) {}

    override fun onCleared() {
        navigator.unregister(this)
        schedulerContext.dispose()
        super.onCleared()
    }
}
