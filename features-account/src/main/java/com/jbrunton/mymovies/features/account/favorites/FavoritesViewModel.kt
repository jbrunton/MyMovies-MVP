package com.jbrunton.mymovies.features.account.favorites

import androidx.lifecycle.viewModelScope
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.doOnNetworkError
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.inject.Container
import com.jbrunton.inject.inject
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            useCase.favorites().collect { result ->
                result.doOnNetworkError(this@FavoritesViewModel::showSnackbarIfCachedValue)
                viewState.postValue(FavoritesViewStateFactory.viewState(result))
            }
        }
    }

    private fun showSnackbarIfCachedValue(failure: AsyncResult.Failure<List<Movie>>) {
        if (failure.cachedValue != null) {
            snackbar.postValue(NetworkErrorSnackbar(retry = true))
        }
    }
}
