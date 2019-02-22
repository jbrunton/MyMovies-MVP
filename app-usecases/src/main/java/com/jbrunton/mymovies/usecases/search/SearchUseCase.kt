package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.SchedulerContext
import com.jbrunton.entities.errors.handleNetworkErrors
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.mymovies.usecases.BaseUseCase
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class SearchUseCase(
        val repository: MoviesRepository
) : BaseUseCase() {
    val results = PublishSubject.create<AsyncResult<SearchState>>()
    private val searches = PublishSubject.create<String>()

    val EmptyQueryResult = AsyncResult.success(SearchState.EmptyQuery)

    override fun start(schedulerContext: SchedulerContext) {
        super.start(schedulerContext)
        schedulerContext.subscribe(searches
                .switchMap(this::doSearch)
                .startWith(EmptyQueryResult),
                results::onNext)
    }

    fun search(query: String) {
        searches.onNext(query)
    }

    private fun doSearch(query: String): DataStream<SearchState> {
        if (query.isEmpty()) {
            return Observable.just(EmptyQueryResult)
        }

        return repository.searchMovies(query)
                .map { SearchState.from(it).handleNetworkErrors() }
                .compose(schedulerContext.applySchedulers())
    }
}
