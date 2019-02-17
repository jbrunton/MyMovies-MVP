package com.jbrunton.mymovies.usecases.favorites

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.errors.doOnNetworkError
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.usecases.search.SearchState
import io.reactivex.subjects.PublishSubject

class FavoritesUseCase(
        val movies: MoviesRepository
) {
    val retrySnackbar = PublishSubject.create<Unit>()

    fun movies(): DataStream<SearchState> {
        return movies.favorites().map {
            SearchState.from(it)
                    .handleNetworkErrors()
                    .doOnNetworkError(this::showSnackbarIfCachedValue)
        }
    }

    private fun showSnackbarIfCachedValue(failure: AsyncResult.Failure<SearchState>) {
        if (failure.cachedValue != null) {
            retrySnackbar.onNext(Unit)
        }
    }
}