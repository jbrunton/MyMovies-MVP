package com.jbrunton.mymovies.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.AsyncResult
import com.jbrunton.entities.models.onSuccess
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.handleNetworkErrors
import com.jbrunton.mymovies.shared.toLoadingViewState

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

    private fun setGenresResponse(state: AsyncResult<List<Genre>>) {
        val viewState = state
                .onSuccess(this::errorIfEmpty)
                .handleNetworkErrors()
                .toLoadingViewState(emptyList())
        this.viewState.postValue(viewState)
    }

    private fun errorIfEmpty(viewState: AsyncResult.Success<GenresViewState>): AsyncResult<GenresViewState> {
        if (viewState.value.isEmpty()) {
            return failure(
                    errorMessage = "Could not load genres at this time",
                    errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
                    allowRetry = true)
        } else {
            return viewState
        }
    }
}
