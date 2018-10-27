package com.jbrunton.mymovies.shared

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable

abstract class BaseLoadingViewModel<T> : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<T>>()

    protected fun <S>load(source: () -> Observable<S>, onSuccess: (S) -> Unit) {
        viewState.postValue(LoadingViewState.Loading)
        source().compose(applySchedulers())
                .subscribe(onSuccess, this::setErrorResponse)
    }

    protected fun setErrorResponse(throwable: Throwable) {
        viewState.value = LoadingViewState.fromError(throwable)
    }
}