package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.entities.safelySubscribe
import com.jbrunton.mymovies.usecases.BaseUseCase
import com.jbrunton.mymovies.usecases.search.SearchState
import io.reactivex.subjects.PublishSubject

class FavoritesUseCase(
        val movies: MoviesRepository
) : BaseUseCase() {
    val retrySnackbar = PublishSubject.create<Unit>()
    val favorites = PublishSubject.create<AsyncResult<SearchState>>()

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)
        loadFavorites()
    }

    fun retry() {
        loadFavorites()
    }

    private fun loadFavorites() {
        movies.favorites()
                .map(this::handleResult)
                .safelySubscribe(this, favorites::onNext)
    }

    private fun handleResult(result: AsyncResult<List<Movie>>): AsyncResult<SearchState> {
        return SearchState.from(result)
                .handleNetworkErrors()
                .doOnNetworkError(this::showSnackbarIfCachedValue)
    }

    private fun showSnackbarIfCachedValue(failure: AsyncResult.Failure<SearchState>) {
        if (failure.cachedValue != null) {
            retrySnackbar.onNext(Unit)
        }
    }
}