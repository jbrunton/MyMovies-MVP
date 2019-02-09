package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar
import com.jbrunton.async.AsyncResult

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()

    open fun retry() {}

    protected fun showSnackbarIfCachedValue(result: AsyncResult.Failure<T>) {
        if (result.cachedValue != null) {
            val message = SnackbarMessage(
                    message = "There was a problem reaching the server",
                    duration = Snackbar.LENGTH_INDEFINITE,
                    actionLabel = "Retry",
                    action = this::retry
            )
            snackbar.postValue(message)
        }
    }
}