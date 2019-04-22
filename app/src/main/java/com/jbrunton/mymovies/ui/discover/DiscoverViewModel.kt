package com.jbrunton.mymovies.ui.discover

import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.MovieDetailsRequest

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

    fun onMovieSelected(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }
}
