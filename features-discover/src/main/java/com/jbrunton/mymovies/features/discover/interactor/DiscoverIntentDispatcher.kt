package com.jbrunton.mymovies.features.discover.interactor

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.libs.ui.viewmodels.Dispatcher
import com.jbrunton.mymovies.usecases.discover.DiscoverUseCase
import com.snakydesign.livedataextensions.liveDataOf
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map

class DiscoverIntentDispatcher(
        private val useCase: DiscoverUseCase,
        private val callbacks: Callbacks
) : Dispatcher<DiscoverIntent, DiscoverStateChange> {

    interface Callbacks {
        fun showMovieDetails(movie: Movie)
    }

    @ExperimentalCoroutinesApi
    override fun dispatch(intent: DiscoverIntent): LiveData<DiscoverStateChange> = when (intent) {
        is DiscoverIntent.Load -> load()
        is DiscoverIntent.SelectGenre -> selectGenre(intent)
        is DiscoverIntent.ClearSelectedGenre -> clearSelectedGenre(intent)
        is DiscoverIntent.SelectMovie -> selectMovie(intent)
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
        callbacks.showMovieDetails(intent.movie)
        return liveDataOf(DiscoverStateChange.Nothing)
    }

    private fun buildGenreResults(genreResults: AsyncResult<List<Movie>>): DiscoverStateChange {
        return DiscoverStateChange.GenreResultsAvailable(genreResults)
    }
}
