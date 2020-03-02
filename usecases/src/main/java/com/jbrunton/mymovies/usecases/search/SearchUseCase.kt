package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.entities.errors.handleNetworkErrors
import com.jbrunton.mymovies.entities.repositories.DataStream
import com.jbrunton.mymovies.entities.repositories.MoviesRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*

class SearchUseCase(
        val repository: MoviesRepository
) {

    @ExperimentalCoroutinesApi
    private val searches = BroadcastChannel<String>(100)

    val EmptyQueryResult = AsyncResult.success(SearchResult.EmptyQuery)

    @ExperimentalCoroutinesApi
    fun search(query: String) {
        searches.offer(query)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    fun results(): DataStream<SearchResult> {
        return searches.asFlow()
                .flatMapConcat { doSearch(it) }
                .onStart { emit(EmptyQueryResult) }
    }

    private suspend fun doSearch(query: String): DataStream<SearchResult> {
        if (query.isEmpty()) {
            return flowOf(EmptyQueryResult)
        }

        return repository.searchMovies(query)
                .map { SearchResult.from(it).handleNetworkErrors() }
    }
}
