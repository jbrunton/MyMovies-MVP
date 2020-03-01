package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.inject.HasContainer
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.SnackbarEvent
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.NavigationResult
import com.jbrunton.mymovies.libs.ui.nav.NavigationResultListener
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewStateError

abstract class BaseViewModel(override val container: Container) : ViewModel(),
        HasContainer, NavigationResultListener
{
    val navigator: Navigator by inject()
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
        super.onCleared()
    }
}
