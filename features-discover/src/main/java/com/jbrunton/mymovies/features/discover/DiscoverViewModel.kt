package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverState

class DiscoverViewModel(container: Container) : BaseViewModel(container), DiscoverListener, DiscoverInteractor.Callbacks {
    val scrollToGenreResults = SingleLiveEvent<Unit>()
    val interactor = DiscoverInteractor(container.get(), container.get(), this)

    val viewState by lazy {
        interactor.state.map { DiscoverViewStateFactory.viewState(it) }
    }

    override fun start() {
        super.start()
        perform(DiscoverIntent.Load)
    }

    fun onRetryClicked() {
        perform(DiscoverIntent.Load)
    }

    override fun perform(intent: DiscoverIntent) = interactor.perform(intent)

    override fun scrollToGenreResults() {
        scrollToGenreResults.call()
    }
}
