package com.jbrunton.mymovies.discover

import androidx.lifecycle.MutableLiveData
import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseViewModel
import com.jbrunton.mymovies.shared.LegacyLoadingViewState
import com.jbrunton.networking.DescriptiveError

class GenreResultsViewModel(private val genreId: String, private val repository: MoviesRepository) : BaseViewModel() {
    val viewState = MutableLiveData<SearchViewState>()
    private val viewStateFactory = SearchViewStateFactory()

    override fun start() {
        viewState.setValue(
                SearchViewState(
                        LegacyLoadingViewState.LOADING_STATE,
                        emptyList()))
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = viewStateFactory.fromList(movies)
    }

    private fun setErrorResponse(throwable: Throwable) {
        viewState.value = viewStateFactory.fromError(DescriptiveError.from(throwable))
    }
}
