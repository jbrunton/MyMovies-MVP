package com.jbrunton.mymovies.libs.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.libs.ui.SnackbarEvent
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import org.koin.core.Koin

abstract class BaseLoadingViewModel<T>(
        navigator: Navigator,
        schedulerFactory: SchedulerFactory
) : BaseViewModel(navigator, schedulerFactory) {
    val viewState = MutableLiveData<LoadingViewState<T>>()

    open fun retry() {}
    
    fun NetworkErrorSnackbar(retry: Boolean) = SnackbarEvent(
            message = "There was a problem with your connection",
            duration = Snackbar.LENGTH_INDEFINITE,
            actionLabel = if (retry) {
                "Retry"
            } else {
                "OK"
            },
            action = if (retry) {
                this::retry
            } else {
                null
            }
    )
}
