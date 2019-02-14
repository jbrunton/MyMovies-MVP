package com.jbrunton.usecases

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.bind
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import io.reactivex.Observable

sealed class SearchState {
    object EmptyQuery: SearchState()
    object NoResults : SearchState()
    object Loading : SearchState()
    data class Failure(val error: Throwable) : SearchState()
    data class Some(val movies: List<Movie>) : SearchState()

    companion object {
        fun from(result: AsyncResult<List<Movie>>): SearchState {
            return result.handleNetworkErrors().bind(onSuccess = {
                if (it.value.isEmpty()) {
                    SearchState.NoResults
                } else {
                    SearchState.Some(it.value)
                }
            }, onLoading = {
                SearchState.Loading
            }, onFailure = {
                SearchState.Failure(it.error)
            })
        }
    }
}

class Search(
        val repository: MoviesRepository,
        val schedulerFactory: SchedulerFactory
) {
    fun reduce(queries: Observable<String>): Observable<SearchState> {
        return queries
                .switchMap(this::search)
                .startWith(SearchState.EmptyQuery)
    }

    private fun search(query: String): Observable<SearchState> {
        if (query.isEmpty()) {
            return Observable.just(SearchState.EmptyQuery)
        }

        return repository.searchMovies(query)
                .map { SearchState.from(it) }
                .compose(schedulerFactory.apply())
    }
}
