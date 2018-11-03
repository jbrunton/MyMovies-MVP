package com.jbrunton.mymovies.discover

import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.repositories.GenresRepository
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

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

    private fun setGenresResponse(genres: List<Genre>) {
        if (genres.isEmpty()) {
            viewState.value = LoadingViewState.Failure(
                    errorMessage = "Could not load genres at this time",
                    errorIcon = R.drawable.ic_sentiment_dissatisfied_black_24dp,
                    showTryAgainButton = true)
        } else {
            viewState.value = LoadingViewState.Success(genres)
        }
    }
}
