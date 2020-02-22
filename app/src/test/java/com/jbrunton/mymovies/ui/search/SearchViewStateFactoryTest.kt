package com.jbrunton.mymovies.ui.search

import android.content.Context
import com.jbrunton.async.AsyncResult
import com.jbrunton.mymovies.R
import com.jbrunton.mymovies.features.search.SearchViewStateFactory
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.libs.ui.viewstates.toLoadingViewState
import com.jbrunton.mymovies.shared.ui.SearchViewState
import com.jbrunton.mymovies.usecases.search.SearchResult
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class SearchViewStateFactoryTest {
    val movie = MovieFactory().create()
    val context = mock<Context> {
        on { getString(R.string.search_no_results) } doReturn "No Results"
        on { getString(R.string.search_empty) } doReturn "Search"
    }
    val factory = SearchViewStateFactory(context)

    @Test
    fun handlesSuccess() {
        val success = AsyncResult.success(SearchResult.Some(listOf(movie)))

        val result = factory.viewState(success)

        val expected = AsyncResult.success(SearchViewState.from(listOf(movie)))
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesNoResults() {
        val failure = AsyncResult.success(SearchResult.NoResults)

        val result = factory.viewState(failure)

        val expected = AsyncResult.failure<SearchViewState>(factory.NoResultsError)
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesEmptyQuery() {
        val failure = AsyncResult.success(SearchResult.EmptyQuery)

        val result = factory.viewState(failure)

        val expected = AsyncResult.failure<SearchViewState>(factory.EmptyStateError)
                .toLoadingViewState(SearchViewState.Empty)
        assertThat(result).isEqualTo(expected)
    }
}
