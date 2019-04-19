package com.jbrunton.mymovies.ui.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.ui.shared.BaseLoadingViewModel

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
        useCase.perform(DiscoverIntent.Load)
    }

    fun onGenreClicked(genre: Genre) {
        useCase.perform(DiscoverIntent.SelectGenre(genre))
    }

    fun onClearGenreSelection() {
        useCase.perform(DiscoverIntent.ClearSelectedGenre)
    }
}
