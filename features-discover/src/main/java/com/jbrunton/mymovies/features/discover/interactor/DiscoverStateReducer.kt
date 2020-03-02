package com.jbrunton.mymovies.features.discover.interactor

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.mymovies.libs.ui.viewmodels.Reducer
import com.jbrunton.mymovies.usecases.discover.DiscoverState

class DiscoverStateReducer(
        val callbacks: Callbacks
) : Reducer<AsyncResult<DiscoverState>, DiscoverStateChange> {

    interface Callbacks {
        fun scrollToGenreResults()
    }

    override fun combine(
            previousState: AsyncResult<DiscoverState>,
            change: DiscoverStateChange
    ) = when (change) {
        is DiscoverStateChange.DiscoverResultsAvailable -> change.discoverResult
        is DiscoverStateChange.GenreSelected -> previousState.map {
            it.copy(selectedGenre = change.selectedGenre)
        }
        is DiscoverStateChange.GenreResultsAvailable -> previousState.map {
            val genreResults = change.genreResults.getOr(emptyList())
            if (genreResults.any()) {
                callbacks.scrollToGenreResults()
            }
            it.copy(genreResults = genreResults)
        }
        is DiscoverStateChange.SelectedGenreCleared -> previousState.map {
            it.copy(genreResults = emptyList(), selectedGenre = null)
        }
        is DiscoverStateChange.Nothing -> previousState
    }
}
