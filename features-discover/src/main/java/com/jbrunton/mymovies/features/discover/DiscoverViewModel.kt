package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.inject.module
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.discover.interactor.*
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.libs.ui.viewmodels.Dispatcher
import com.jbrunton.mymovies.libs.ui.viewmodels.Interactor
import com.jbrunton.mymovies.libs.ui.viewmodels.Reducer
import com.jbrunton.mymovies.usecases.discover.DiscoverState

interface DiscoverIntentListener {
    fun perform(intent: DiscoverIntent)
}

class DiscoverViewModel(container: Container) : BaseViewModel(container),
        DiscoverIntentListener,
        DiscoverInteractor.Callbacks
{
    val scrollToGenreResults = SingleLiveEvent<Unit>()

    val viewState by lazy { interactor.state.map(DiscoverViewStateFactory::viewState) }

    private val interactor: DiscoverInteractor by lazy {
        val initialState = AsyncResult.loading(null)
        DiscoverInteractor(initialState, container.get(), this)
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

    override fun showMovieDetails(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }
}
