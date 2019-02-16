package com.jbrunton.mymovies.ui.discover

import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase

class DiscoverViewModel(val useCase: com.jbrunton.mymovies.usecases.discover.DiscoverUseCase) : BaseLoadingViewModel<DiscoverViewState>() {
    override fun start() {
        load()
    }

    override fun retry() {
        load()
    }

    private fun load() {
        subscribe(useCase.reduce()) {
            viewState.postValue(DiscoverViewStateFactory.from(it))
        }
    }
}
