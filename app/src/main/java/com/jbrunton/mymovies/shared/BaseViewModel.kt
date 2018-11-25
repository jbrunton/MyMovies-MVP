package com.jbrunton.mymovies.shared

import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModel
import com.jbrunton.entities.models.Result
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

    fun<T>failure(errorMessage: String,
                  @DrawableRes errorIcon: Int = 0,
                  allowRetry: Boolean = false
    ): Result<T> {
        return Result.Failure(LoadingViewStateError(errorMessage, errorIcon, allowRetry))
    }
}
