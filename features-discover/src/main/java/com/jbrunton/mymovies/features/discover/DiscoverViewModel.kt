package com.jbrunton.mymovies.features.discover

import androidx.lifecycle.*
import com.jbrunton.async.AsyncResult
import com.jbrunton.async.getOr
import com.jbrunton.async.map
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.safelySubscribe
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseViewModel
import com.jbrunton.mymovies.libs.ui.viewstates.LoadingViewState
import com.jbrunton.mymovies.usecases.discover.DiscoverResult
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.snakydesign.livedataextensions.scan
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.rx2.collect

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
    data class DiscoverResultsAvailable(val discoverResult: AsyncResult<DiscoverResult>) : DiscoverStateChange()
    data class GenreSelected(val selectedGenre: Genre) : DiscoverStateChange()
    data class GenreResultsAvailable(val genreResults: AsyncResult<List<Movie>>) : DiscoverStateChange()
    object SelectedGenreCleared : DiscoverStateChange()
    object Nothing : DiscoverStateChange()
}

class DiscoverViewModel(container: Container) : BaseViewModel(container), DiscoverListener {
    val scrollToGenreResults = SingleLiveEvent<Unit>()

    private val useCase: DiscoverUseCase by inject()
    private val changes = MediatorLiveData<DiscoverStateChange>()

    val viewState by lazy {
        val initialState = AsyncResult.loading(null)
        val state = changes.scan(initialState, ::reduce)
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

    private fun reduce(previousState: AsyncResult<DiscoverResult>, change: DiscoverStateChange): AsyncResult<DiscoverResult> {
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
