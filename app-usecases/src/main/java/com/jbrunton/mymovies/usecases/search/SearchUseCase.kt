package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable

class SearchUseCase(
        val repository: MoviesRepository,
        val schedulerContext: SchedulerContext
) {
    fun reduce(queries: Observable<String>): DataStream<SearchState> {
        return queries
                .switchMap(this::search)
                .startWith(AsyncResult.success(SearchState.EmptyQuery))
    }

    private fun search(query: String): DataStream<SearchState> {
        if (query.isEmpty()) {
            return Observable.just(AsyncResult.success(SearchState.EmptyQuery))
        }

        return repository.searchMovies(query)
                .map { SearchState.from(it).handleNetworkErrors() }
                .compose(schedulerContext.applySchedulers())
    }
}
