package com.jbrunton.mymovies.ui.shared

import androidx.lifecycle.MutableLiveData
import com.jbrunton.async.AsyncResult
import io.reactivex.Observable

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()
    val showRetrySnackbar = SingleLiveEvent<Unit>()

    protected fun <S>load(source: () -> Observable<S>, onSuccess: (S) -> Unit) {
        source().compose(applySchedulers())
                .subscribe(onSuccess, this::setErrorResponse)
    }

    protected fun setErrorResponse(throwable: Throwable) {
        throw throwable
    }

    protected fun showSnackbarIfCachedValue(result: AsyncResult.Failure<T>) {
        if (result.cachedValue != null) {
            showRetrySnackbar.call()
        }
    }
}