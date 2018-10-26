package com.jbrunton.mymovies.discover

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Genre
import com.jbrunton.entities.GenresRepository
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class GenresViewModel(private val repository: GenresRepository) : BaseViewModel() {
    val viewState = MutableLiveData<GenresViewState>()
    private val converter = GenresViewStateFactory()

    override fun start() {
        repository.genres()
                .compose(applySchedulers())
                .subscribe(this::setGenresResponse, this::setErrorResponse)
    }

    private fun setGenresResponse(genres: List<Genre>) {
        viewState.value = converter.fromList(genres)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = LoadingViewState.fromError(throwable)
    }
}
