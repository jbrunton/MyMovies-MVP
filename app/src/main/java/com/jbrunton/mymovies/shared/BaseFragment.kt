package com.jbrunton.mymovies.shared

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import com.jbrunton.mymovies.ApplicationDependencies
import com.jbrunton.mymovies.MyMoviesApplication
import com.jbrunton.mymovies.helpers.toVisibility
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseFragment<T : BaseViewModel> : Fragment() {
    lateinit var viewModel: T
            private set

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = provideViewModel()
        viewModel.start()
    }

    protected fun dependencies(): ApplicationDependencies {
        return (activity!!.application as MyMoviesApplication).dependencies
    }

    protected abstract fun provideViewModel(): T

    protected fun getViewModel(modelClass: Class<T>, factory: ViewModelProvider.Factory): T {
        return ViewModelProviders.of(this, factory).get(modelClass)
    }

    fun updateLoadingState(viewState: LoadingViewState) {
        loading_indicator.visibility = toVisibility(viewState.showLoadingIndicator())
        error_case.visibility = toVisibility(viewState.showError());
        error_text.text = viewState.errorMessage();
        error_try_again.visibility = toVisibility(viewState.showTryAgainButton());
        error_image.setImageResource(viewState.errorIcon());
    }
}
