package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class SearchUseCase(
        val repository: MoviesRepository
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
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}