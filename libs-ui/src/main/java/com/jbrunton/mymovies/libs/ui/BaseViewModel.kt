package com.jbrunton.mymovies.libs.ui

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.HasSchedulers
import com.jbrunton.mymovies.entities.SchedulerContext
import com.jbrunton.mymovies.entities.SchedulerFactory

abstract class BaseViewModel(override val container: Container) : ViewModel(),
        HasSchedulers, HasContainer, NavigationResultListener
{
    val schedulerFactory: SchedulerFactory by inject()
    val navigator: Navigator by inject()
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
