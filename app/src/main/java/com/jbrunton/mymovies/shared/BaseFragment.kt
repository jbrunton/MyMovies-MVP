package com.jbrunton.mymovies.shared

import com.jbrunton.mymovies.helpers.toVisibility
import kotlinx.android.synthetic.main.layout_loading_state.*

abstract class BaseFragment<T : BaseViewModel> : androidx.fragment.app.Fragment() {

    fun updateLoadingView(viewState: LoadingViewState<*>) {
        loading_indicator.visibility = toVisibility(viewState is Loading)
        error_case.visibility = toVisibility(viewState is Failure)
        when (viewState) {
            is Failure -> {
                error_text.text = viewState.errorMessage
                error_try_again.visibility = toVisibility(viewState.showTryAgainButton)
                error_image.setImageResource(viewState.errorIcon)
            }
        }
    }
}
