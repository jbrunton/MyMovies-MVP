package com.jbrunton.mymovies.shared

import android.support.v7.app.AppCompatActivity
import com.jbrunton.mymovies.helpers.toVisibility
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    protected fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer {
            observable -> observable
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun updateLoadingView(viewState: LoadingViewState) {
        loading_indicator.visibility = toVisibility(viewState.showLoadingIndicator())
        error_case.visibility = toVisibility(viewState.showError())
        error_text.text = viewState.errorMessage()
        error_try_again.visibility = toVisibility(viewState.showTryAgainButton())
        error_image.setImageResource(viewState.errorIcon())
    }
}
