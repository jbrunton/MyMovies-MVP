package com.jbrunton.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.usecases.SchedulerFactory
import io.reactivex.Observable

class SearchUseCase(
        val repository: MoviesRepository,
        val schedulerFactory: SchedulerFactory
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
                .map { SearchState.from(it) }
                .compose(schedulerFactory.apply())
    }
}
