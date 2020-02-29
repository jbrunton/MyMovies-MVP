package com.jbrunton.mymovies.features.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.usecases.discover.DiscoverState

class DiscoverViewStateReducer(val scrollToGenreResults: SingleLiveEvent<Unit>) {
    fun reduce(previousState: AsyncResult<DiscoverState>, change: DiscoverStateChange): AsyncResult<DiscoverState> {
        return when (change) {
            is DiscoverStateChange.DiscoverResultsAvailable -> change.discoverResult
            is DiscoverStateChange.GenreSelected -> previousState.map {
                it.copy(selectedGenre = change.selectedGenre)
            }
            is DiscoverStateChange.GenreResultsAvailable -> previousState.map {
                val genreResults = change.genreResults.getOr(emptyList())
                if (genreResults.any()) {
                    scrollToGenreResults.call()
                }
                it.copy(genreResults = genreResults)
            }
            is DiscoverStateChange.SelectedGenreCleared -> previousState.map {
                it.copy(genreResults = emptyList(), selectedGenre = null)
            }
            is DiscoverStateChange.Nothing -> previousState
        }
    }
}
