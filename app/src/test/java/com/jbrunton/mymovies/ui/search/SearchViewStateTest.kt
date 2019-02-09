package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult.Companion.failure
import com.jbrunton.async.AsyncResult.Companion.success
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.ui.shared.networkError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

class SearchViewStateTest {
    val movie = MovieFactory().create()

    @Test
    fun handlesSuccess() {
        val result = SearchViewState.Builder(success(listOf(movie))).asResult()
        assertThat(result).isEqualTo(success(SearchViewState.from(listOf(movie))))
    }

    @Test
    fun handlesEmptyList() {
        val result = SearchViewState.Builder(success(emptyList())).asResult()
        assertThat(result).isEqualTo(SearchViewState.Builder.NoResults)
    }

    @Test
    fun handlesNetworkErrors() {
        val result = SearchViewState.Builder(failure(IOException())).asResult()
        assertThat(result).isEqualTo(failure<SearchViewState>(networkError()))
    }
}