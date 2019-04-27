package com.jbrunton.mymovies.features.account.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.doOnNetworkError
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.movies.list.SearchViewState
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase

class FavoritesViewModel(container: Container) : BaseLoadingViewModel<SearchViewState>(container) {
    val useCase: FavoritesUseCase by inject()

    override fun start() {
        loadFavorites()
    }

    override fun retry() {
        loadFavorites()
    }

    fun onMovieSelected(movie: Movie) {
        navigator.navigate(MovieDetailsRequest(movie.id))
    }

    private fun loadFavorites() {
        subscribe(useCase.favorites()) { result ->
            result.doOnNetworkError(this::showSnackbarIfCachedValue)
            viewState.postValue(FavoritesViewStateFactory.viewState(result))
        }
    }

    private fun showSnackbarIfCachedValue(failure: AsyncResult.Failure<List<Movie>>) {
        if (failure.cachedValue != null) {
            snackbar.postValue(NetworkErrorSnackbar(retry = true))
        }
    }
}
