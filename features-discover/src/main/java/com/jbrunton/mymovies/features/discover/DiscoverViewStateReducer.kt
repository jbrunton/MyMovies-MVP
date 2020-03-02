package com.jbrunton.mymovies.features.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.viewmodels.Interactor
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import kotlinx.coroutines.flow.Flow

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    data class SelectMovie(val movie: Movie) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverState>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
    object Nothing : DiscoverStateChange()
}

interface DiscoverListener {
    fun perform(intent: DiscoverIntent)
}

class DiscoverInteractor(
        val scrollToGenreResults: SingleLiveEvent<Unit>
) : Interactor<DiscoverIntent, AsyncResult<DiscoverState>, DiscoverStateChange>() {

    override val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)

    override suspend fun actionsFor(intent: DiscoverIntent): Flow<DiscoverStateChange> {

    }

    override fun combine(previousState: AsyncResult<DiscoverState>, change: DiscoverStateChange): AsyncResult<DiscoverState> {
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
