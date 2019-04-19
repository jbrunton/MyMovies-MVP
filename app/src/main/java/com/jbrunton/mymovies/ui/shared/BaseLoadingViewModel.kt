package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.inject.Container
import com.jbrunton.libs.ui.LoadingViewState
import com.jbrunton.libs.ui.SnackbarEvent

abstract class BaseLoadingViewModel<T>(container: Container) : BaseViewModel(container) {
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
