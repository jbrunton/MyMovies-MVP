package com.jbrunton.mymovies.ui.discover

import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase

class DiscoverViewModel(container: Container) : BaseLoadingViewModel<DiscoverViewState>(container) {
    val useCase: DiscoverUseCase by inject()

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
