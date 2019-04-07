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
        super.start()
        subscribe(useCase.state) {
            viewState.postValue(DiscoverViewStateFactory.viewState(it))
        }
        start(useCase)
    }

    fun onRetryClicked() {
        useCase.retry()
    }

    fun onGenreClicked(genre: Genre) {
        useCase.showGenre(genre)
    }

    fun onClearGenreSelection() {
        useCase.clearGenreSelection()
    }
}
