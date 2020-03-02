package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.snakydesign.livedataextensions.emptyLiveData
import com.snakydesign.livedataextensions.liveDataOf
import com.snakydesign.livedataextensions.scan
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

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
        val useCase: DiscoverUseCase,
        val navigator: Navigator,
        val scrollToGenreResults: SingleLiveEvent<Unit>
) : Interactor<DiscoverIntent, AsyncResult<DiscoverState>, DiscoverStateChange>() {
    private val reducer = DiscoverViewStateReducer(scrollToGenreResults)

    override val initialState: AsyncResult<DiscoverState> = AsyncResult.loading(null)

    @ExperimentalCoroutinesApi
    override fun dispatch(intent: DiscoverIntent): LiveData<DiscoverStateChange> = when (intent) {
        is DiscoverIntent.Load -> load()
        is DiscoverIntent.SelectGenre -> selectGenre(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenre(intent)
        is DiscoverIntent.SelectMovie -> selectMovie(intent)
    }

    override fun combine(previousState: AsyncResult<DiscoverState>, change: DiscoverStateChange): AsyncResult<DiscoverState> {
        return reducer.reduce(previousState, change)
    }

    @ExperimentalCoroutinesApi
    private fun load() = liveData {
        useCase.discover()
                .map { DiscoverStateChange.DiscoverResultsAvailable(it) as DiscoverStateChange }
                .collect { emit(it) }
    }

    private fun selectGenre(intent: DiscoverIntent.SelectGenre) = liveData {
        val selectedChange: DiscoverStateChange = DiscoverStateChange.GenreSelected(intent.genre)
        emit(selectedChange)
        useCase.discoverByGenre(intent.genre.id)
                    .map { buildGenreResults(it) }
                    .collect { emit(it) }
    }

    private fun clearSelectedGenre(intent: DiscoverIntent.ClearSelectedGenre): LiveData<DiscoverStateChange> {
        return liveDataOf(DiscoverStateChange.SelectedGenreCleared)
    }

    private fun selectMovie(intent: DiscoverIntent.SelectMovie): LiveData<DiscoverStateChange> {
        navigator.navigate(MovieDetailsRequest(intent.movie.id))
        return liveDataOf(DiscoverStateChange.Nothing)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }

}

class DiscoverViewModel(container: Container) : BaseViewModel(container), DiscoverListener {
    val scrollToGenreResults = SingleLiveEvent<Unit>()
    val interactor = DiscoverInteractor(container.get(), container.get(), scrollToGenreResults)

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
}
