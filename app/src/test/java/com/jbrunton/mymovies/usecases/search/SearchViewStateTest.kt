package com.jbrunton.mymovies.usecases.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.ui.search.SearchViewState
import com.jbrunton.mymovies.ui.search.SearchViewStateFactory
import com.jbrunton.mymovies.ui.shared.toLoadingViewState
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SearchViewStateTest {
    val movie = MovieFactory().create()

    @Test
    fun handlesSuccess() {
        val success = AsyncResult.success(SearchState.Some(listOf(movie)))

        val result = SearchViewStateFactory.viewState(success)

        val expected = AsyncResult.success(SearchViewState.from(listOf(movie)))
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesNoResults() {
        val failure = AsyncResult.success(SearchState.NoResults)

        val result = SearchViewStateFactory.viewState(failure)

        val expected = AsyncResult.failure<SearchViewState>(SearchViewStateFactory.NoResultsError)
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesEmptyQuery() {
        val failure = AsyncResult.success(SearchState.EmptyQuery)

        val result = SearchViewStateFactory.viewState(failure)

        val expected = AsyncResult.failure<SearchViewState>(SearchViewStateFactory.EmptyStateError)
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }
}
