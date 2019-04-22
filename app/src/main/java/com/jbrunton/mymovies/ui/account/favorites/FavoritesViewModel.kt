package com.jbrunton.mymovies.ui.account.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.subscribe
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.libs.ui.BaseLoadingViewModel
import com.jbrunton.libs.ui.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.SearchViewState
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
