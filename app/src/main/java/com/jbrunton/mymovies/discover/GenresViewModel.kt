package com.jbrunton.mymovies.discover

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.jbrunton.entities.Genre
import com.jbrunton.entities.GenresRepository
import com.jbrunton.mymovies.shared.BaseViewModel

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
        viewState.value = converter.fromError(throwable)
    }

    class Factory(private val repository: GenresRepository) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {

            return GenresViewModel(repository) as T
        }
    }
}
