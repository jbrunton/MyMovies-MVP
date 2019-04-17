package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerFactory
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchUseCase(
        val repository: MoviesRepository,
        val schedulerFactory: SchedulerFactory
) {
    private val searches = PublishSubject.create<String>()

    val EmptyQueryResult = AsyncResult.success(SearchState.EmptyQuery)

    fun search(query: String) {
        searches.onNext(query)
    }

    fun results(): DataStream<SearchState> {
        return searches
                .switchMap(this::doSearch)
                .startWith(EmptyQueryResult)
    }

    private fun doSearch(query: String): DataStream<SearchState> {
        if (query.isEmpty()) {
            return Observable.just(EmptyQueryResult)
        }

        return repository.searchMovies(query)
                .map { SearchState.from(it).handleNetworkErrors() }
                .subscribeOn(schedulerFactory.IO)
    }
}
