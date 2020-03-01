package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
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

class DiscoverViewModel(container: Container) : BaseViewModel(container), DiscoverListener {
    val scrollToGenreResults = SingleLiveEvent<Unit>()
    private val useCase: DiscoverUseCase by inject()
    private val changes = MediatorLiveData<DiscoverStateChange>()
    private val reducer = DiscoverViewStateReducer(scrollToGenreResults)

    val viewState by lazy {
        val initialState = AsyncResult.loading(null)
        val state = changes.scan(initialState, reducer::reduce)
                .distinctUntilChanged()
        state.map { DiscoverViewStateFactory.viewState(it) }
    }

    override fun start() {
        super.start()
        perform(DiscoverIntent.Load)
    }

    fun onRetryClicked() {
        perform(DiscoverIntent.Load)
    }

    override fun perform(intent: DiscoverIntent) = when (intent) {
        is DiscoverIntent.Load -> load(intent)
        is DiscoverIntent.SelectGenre -> selectGenre(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenre(intent)
        is DiscoverIntent.SelectMovie -> selectMovie(intent)
    }

    @ExperimentalCoroutinesApi
    private fun load(intent: DiscoverIntent.Load) {
        viewModelScope.launch {
            useCase.discover()
                    .map { DiscoverStateChange.DiscoverResultsAvailable(it) }
                    .collect { changes.postValue(it) }
        }
    }

    private fun selectGenre(intent: DiscoverIntent.SelectGenre) {
        val selectedChange: DiscoverStateChange = DiscoverStateChange.GenreSelected(intent.genre)
        changes.postValue(selectedChange)
        viewModelScope.launch {
            useCase.discoverByGenre(intent.genre.id)
                    .map { buildGenreResults(it) }
                    .collect { changes.postValue(it) }
        }
    }

    private fun clearSelectedGenre(intent: DiscoverIntent.ClearSelectedGenre) {
        return changes.postValue(DiscoverStateChange.SelectedGenreCleared)
    }

    private fun selectMovie(intent: DiscoverIntent.SelectMovie) {
        navigator.navigate(MovieDetailsRequest(intent.movie.id))
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }
}
