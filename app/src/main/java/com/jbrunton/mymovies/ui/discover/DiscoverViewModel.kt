package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase

class DiscoverViewModel(container: Container) : BaseLoadingViewModel<DiscoverViewState>(container) {
    val useCase: DiscoverUseCase by inject()

    override fun start() {
        subscribe(useCase.state) {
            viewState.postValue(DiscoverViewStateFactory.viewState(it))
        }
        start(useCase)
    }

    override fun retry() {
        useCase.retry()
    }

    fun showGenre(genre: Genre) {
        useCase.showGenre(genre)
    }
}
