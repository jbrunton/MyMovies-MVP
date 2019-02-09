package com.jbrunton.mymovies.ui.search

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.ui.shared.networkError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.io.IOException

class SearchViewStateTest {
    val movie = MovieFactory().create()
    val success = AsyncResult.success(listOf(movie))
    val emptyResult = AsyncResult.success(emptyList<Movie>())
    val error = AsyncResult.failure<List<Movie>>(IOException())

    @Test
    fun handlesSuccess() {
        val result = SearchViewState.Builder(success).asResult()
        assertThat(result).isEqualTo(
                AsyncResult.success(
                        SearchViewState(listOf(MovieSearchResultViewState(movie)))
                )
        )
    }

    @Test
    fun handlesEmptyList() {
        val result = SearchViewState.Builder(emptyResult).asResult()
        assertThat(result).isEqualTo(SearchViewState.Builder.NoResults)
    }

    @Test
    fun handlesNetworkErrors() {
        val result = SearchViewState.Builder(error).asResult()
        assertThat(result).isEqualTo(AsyncResult.failure<SearchViewState>(networkError()))
    }
}