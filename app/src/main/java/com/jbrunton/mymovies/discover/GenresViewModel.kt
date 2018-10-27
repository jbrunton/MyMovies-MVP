package com.jbrunton.mymovies.discover

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Genre
import com.jbrunton.entities.GenresRepository
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class GenresViewModel(private val repository: GenresRepository) : BaseViewModel() {
    val viewState = MutableLiveData<GenresViewState>()

    override fun start() {
        repository.genres()
                .compose(applySchedulers())
                .subscribe(this::setGenresResponse, this::setErrorResponse)
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

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = LoadingViewState.fromError(throwable)
    }
}
