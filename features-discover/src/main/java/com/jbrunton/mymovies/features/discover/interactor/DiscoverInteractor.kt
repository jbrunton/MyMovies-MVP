package com.jbrunton.mymovies.features.discover.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewmodels.Dispatcher
import com.jbrunton.mymovies.libs.ui.viewmodels.Interactor
import com.jbrunton.mymovies.libs.ui.viewmodels.Reducer
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.snakydesign.livedataextensions.liveDataOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

sealed class DiscoverIntent {
    object Load : DiscoverIntent()
    data class SelectGenre(val genre: Genre) : DiscoverIntent()
    data class SelectMovie(val movie: Movie) : DiscoverIntent()
    object ClearSelectedGenre : DiscoverIntent()
}

interface DiscoverListener {
    fun perform(intent: DiscoverIntent)
}

sealed class DiscoverStateChange {
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverState>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
    object Nothing : DiscoverStateChange()
}

class DiscoverInteractor(
        useCase: DiscoverUseCase,
        callbacks: Callbacks
) : Interactor<DiscoverIntent, AsyncResult<DiscoverState>, DiscoverStateChange>(),
        Reducer<AsyncResult<DiscoverState>, DiscoverStateChange> by DiscoverStateReducer(callbacks),
        Dispatcher<DiscoverIntent, DiscoverStateChange> by DiscoverIntentDispatcher(useCase, callbacks)
{
    interface Callbacks : DiscoverIntentDispatcher.Callbacks, DiscoverStateReducer.Callbacks

    override val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)
}
