package com.jbrunton.mymovies.shared

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jbrunton.mymovies.ApplicationDependencies
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.toVisibility
import io.reactivex.ObservableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {
    lateinit var viewModel: T
            private set

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.start()
    }

    protected fun dependencies(): ApplicationDependencies {
        return (application as MyMoviesApplication).dependencies
    }

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

    protected abstract fun provideViewModel(): T

    protected fun getViewModel(modelClass: Class<T>, factory: ViewModelProvider.Factory): T {
        return ViewModelProviders.of(this, factory).get(modelClass)
    }
}
