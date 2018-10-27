package com.jbrunton.mymovies.shared

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }
}
