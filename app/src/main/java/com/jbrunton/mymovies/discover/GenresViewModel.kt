package com.jbrunton.mymovies.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.LoadingState
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState
import com.jbrunton.mymovies.shared.map
import com.jbrunton.mymovies.shared.toViewState

class GenresViewModel(private val repository: GenresRepository) : BaseLoadingViewModel<GenresViewState>() {
    override fun start() {
        loadGenres()
    }

    fun retry() {
        loadGenres()
    }

    private fun loadGenres() {
        load(repository::genres, this::setGenresResponse)
    }

    private fun setGenresResponse(state: LoadingState<List<Genre>>) {
        val viewState = state
                .toViewState()
                .map(this::errorIfEmpty)
        this.viewState.postValue(viewState)
    }

    private fun errorIfEmpty(genres: List<Genre>): LoadingViewState<GenresViewState> {
        if (genres.isEmpty()) {
            return LoadingViewState.Failure(
                    errorMessage = "Could not load genres at this time",
                    errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
                    showTryAgainButton = true)
        } else {
            return LoadingViewState.Success(genres)
        }
    }
}
