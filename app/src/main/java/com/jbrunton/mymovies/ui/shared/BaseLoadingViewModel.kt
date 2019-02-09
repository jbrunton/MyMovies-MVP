package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.jbrunton.async.AsyncResult

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()
    val showRetrySnackbar = SingleLiveEvent<Unit>()

    protected fun showSnackbarIfCachedValue(result: AsyncResult.Failure<T>) {
        if (result.cachedValue != null) {
            showRetrySnackbar.call()
        }
    }
}