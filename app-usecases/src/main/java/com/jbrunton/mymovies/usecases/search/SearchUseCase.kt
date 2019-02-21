package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchUseCase(
        val repository: MoviesRepository,
        val schedulerContext: SchedulerContext
) {
    val results = PublishSubject.create<AsyncResult<SearchState>>()

    fun start(queries: Observable<String>) {
        val observable = queries
                .switchMap(this::search)
                .startWith(AsyncResult.success(SearchState.EmptyQuery))
        schedulerContext.subscribe(observable, results::onNext)
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
