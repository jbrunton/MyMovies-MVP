package com.jbrunton.usecases

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.map
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.DataStream
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable

sealed class SearchState {
    object EmptyQuery: SearchState()
    object NoResults : SearchState()
    data class Some(val movies: List<Movie>) : SearchState()

    companion object {
        fun from(result: AsyncResult<List<Movie>>): AsyncResult<SearchState> {
            return result.handleNetworkErrors().map {
                if (it.isEmpty()) {
                    SearchState.NoResults
                } else {
                    SearchState.Some(it)
                }
            }
        }
    }
}

class Search(
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
