package com.jbrunton.mymovies.shared

import android.support.v4.app.Fragment
import com.jbrunton.mymovies.helpers.toVisibility
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseFragment<T : BaseViewModel> : Fragment() {
    fun updateLoadingView(viewState: LoadingViewState) {
        loading_indicator.visibility = toVisibility(viewState.showLoadingIndicator())
        error_case.visibility = toVisibility(viewState.showError())
        error_text.text = viewState.errorMessage()
        error_try_again.visibility = toVisibility(viewState.showTryAgainButton())
        error_image.setImageResource(viewState.errorIcon())
    }
}
