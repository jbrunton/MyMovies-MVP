package com.jbrunton.mymovies.discover

import com.jbrunton.entities.Movie
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.mymovies.search.SearchViewState
import com.jbrunton.mymovies.search.SearchViewStateFactory
import com.jbrunton.mymovies.shared.BaseLoadingViewModel
import com.jbrunton.mymovies.shared.LoadingViewState

class GenreResultsViewModel(val genreId: String, val repository: MoviesRepository) : BaseLoadingViewModel<SearchViewState>() {
    override fun start() {
        viewState.setValue(LoadingViewState.Loading)
        repository.discoverByGenre(genreId)
                .compose(applySchedulers())
                .subscribe(this::setMoviesResponse, this::setErrorResponse)
    }

    private fun setMoviesResponse(movies: List<Movie>) {
        viewState.value = SearchViewStateFactory.fromList(movies)
    }
}
