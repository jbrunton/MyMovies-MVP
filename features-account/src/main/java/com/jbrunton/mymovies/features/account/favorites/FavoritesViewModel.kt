package com.jbrunton.mymovies.features.account.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.SchedulerFactory
import com.jbrunton.mymovies.entities.errors.doOnNetworkError
import com.jbrunton.mymovies.entities.models.Movie
import com.jbrunton.mymovies.entities.subscribe
import com.jbrunton.mymovies.libs.ui.viewmodels.BaseLoadingViewModel
import com.jbrunton.mymovies.libs.ui.nav.MovieDetailsRequest
import com.jbrunton.mymovies.libs.ui.nav.Navigator
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.favorites.FavoritesUseCase
import org.koin.core.Koin
import org.koin.core.inject

class FavoritesViewModel(
        val useCase: FavoritesUseCase,
        navigator: Navigator,
        schedulerFactory: SchedulerFactory
) : BaseLoadingViewModel<SearchViewState>(navigator, schedulerFactory) {
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
