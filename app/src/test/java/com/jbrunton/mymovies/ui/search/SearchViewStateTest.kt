package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.ui.shared.networkError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

class SearchViewStateTest {
    val movie = MovieFactory().create()

    @Test
    fun handlesSuccess() {
        val success = AsyncResult.success(listOf(movie))

        val result = SearchViewStateFactory.map(success)

        val expected = AsyncResult.success(SearchViewState.from(listOf(movie)))
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesEmptyList() {
        val empty = AsyncResult.success(emptyList<Movie>())

        val result = SearchViewStateFactory.map(empty)

        val expected = AsyncResult.failure<SearchViewState>(SearchViewStateFactory.NoResultsError)
        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesNetworkErrors() {
        val failure = AsyncResult.failure<List<Movie>>(IOException())

        val result = SearchViewStateFactory.map(failure)

        val expected = AsyncResult.failure<SearchViewState>(networkError())
        assertThat(result).isEqualTo(expected)
    }
}