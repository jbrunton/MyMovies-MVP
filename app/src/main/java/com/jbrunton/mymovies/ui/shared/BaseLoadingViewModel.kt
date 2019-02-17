package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.google.android.material.snackbar.Snackbar

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()

    open fun retry() {}


    fun NetworkErrorSnackbar(retry: Boolean) = SnackbarMessage(
            message = "There was a problem with your connection",
            duration = Snackbar.LENGTH_INDEFINITE,
            actionLabel = if (retry) { "Retry" } else { null },
            action = if (retry) { this::retry } else { null }
    )
}