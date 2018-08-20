package com.jbrunton.mymovies.shared

import android.arch.lifecycle.ViewModel
import com.jbrunton.networking.DescriptiveError
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Job
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

    protected fun launchSafely(block: SafeLauncherBuilder.() -> Unit): Job = SafeLauncherBuilder().apply(block).build()

    class SafeLauncherBuilder {
        private var _action: (suspend () -> Unit)? = null
        private var _onError: (suspend (DescriptiveError) -> Unit)? = null

        fun action(action: suspend () -> Unit) {
            _action = action
        }

        fun onError(onError: suspend (DescriptiveError) -> Unit) {
            _onError = onError
        }

        fun build(): Job = launch(UI) {
            if (_action == null) {
                throw IllegalStateException("Error: you need to specify an action")
            }
            if (_onError == null) {
                throw IllegalStateException("Error: you need to specify an error handler.")
            }
            launch(UI) {
                try {
                    _action?.invoke()
                } catch (e : DescriptiveError) {
                    _onError?.invoke(e)
                }
            }
        }
    }
}
