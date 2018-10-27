package com.jbrunton.mymovies.discover

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.Loading
import com.jbrunton.mymovies.shared.LoadingViewState

class GenreResultsViewModel(private val genreId: String, private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<LoadingViewState<SearchViewState>>()

    override fun start() {
        viewState.setValue(Loading())
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = SearchViewStateFactory.fromList(movies)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = LoadingViewState.fromError(throwable)
    }
}
