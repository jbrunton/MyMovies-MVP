package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.features.discover.interactor.DiscoverIntent
import com.jbrunton.mymovies.features.discover.interactor.DiscoverInteractor
import com.jbrunton.mymovies.features.discover.interactor.DiscoverListener
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.nav.NavigationRequest
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel

class DiscoverViewModel(container: Container) : BaseViewModel(container),
        DiscoverListener,
        DiscoverInteractor.Callbacks
{
    val scrollToGenreResults = SingleLiveEvent<Unit>()
    private val interactor = DiscoverInteractor(container.get(), this)

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

    override fun showMovieDetails(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }
}
