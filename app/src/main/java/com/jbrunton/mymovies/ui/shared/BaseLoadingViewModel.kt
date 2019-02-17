package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()

    open fun retry() {}

    val RetrySnackbar = SnackbarMessage(
            message = "There was a problem with your connection",
            duration = Snackbar.LENGTH_INDEFINITE,
            actionLabel = "Retry",
            action = this::retry
    )
}