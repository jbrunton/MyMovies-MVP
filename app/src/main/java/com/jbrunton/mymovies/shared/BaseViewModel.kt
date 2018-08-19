package com.jbrunton.mymovies.shared

import android.arch.lifecycle.ViewModel
import com.jbrunton.networking.DescriptiveError
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.android.UI
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            it.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    abstract fun start()

    protected fun launchSafely(onError: (Throwable) -> Unit, block: suspend (Unit) -> Unit) {
        launch(UI) {
            try {
                block.invoke(Unit)
            } catch (e : DescriptiveError) {
                onError.invoke(e)
            }
        }
    }
}
