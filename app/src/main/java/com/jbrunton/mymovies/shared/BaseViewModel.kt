package com.jbrunton.mymovies.shared

import android.arch.lifecycle.ViewModel
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseViewModel : ViewModel() {
    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    abstract fun start()
}
