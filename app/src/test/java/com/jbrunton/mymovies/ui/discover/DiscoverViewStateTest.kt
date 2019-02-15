package com.jbrunton.mymovies.ui.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Genre
import com.jbrunton.entities.models.Movie
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.ui.movies.MovieSearchResultViewState
import com.jbrunton.mymovies.usecases.networkError
import org.assertj.core.api.Assertions
import org.junit.Test
import java.io.IOException

class DiscoverViewStateTest {
    val factory = MovieFactory()

    val nowShowingMovie = factory.create()
    val popularMovie = factory.create()
    val genre = Genre("1", "Action")

    val nowShowingSuccess = AsyncResult.success(listOf(nowShowingMovie))
    val popularSuccess = AsyncResult.success(listOf(popularMovie))
    val genresSuccess = AsyncResult.success(listOf(genre))
    val moviesFailure = AsyncResult.failure<List<Movie>>(IOException())

    @Test
    fun handlesSuccess() {
        val result = DiscoverViewStateFactory.map(nowShowingSuccess, popularSuccess, genresSuccess)

        val expected = AsyncResult.success(
                DiscoverViewState(
                        listOf(MovieSearchResultViewState(nowShowingMovie)),
                        listOf(MovieSearchResultViewState(popularMovie)),
                        listOf(genre)
                )
        )
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesNowShowingNetworkErrors() {
        val result = DiscoverViewStateFactory.map(moviesFailure, popularSuccess, genresSuccess)
        val expected = AsyncResult.failure<DiscoverViewState>(networkError())
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesPopularNetworkErrors() {
        val result = DiscoverViewStateFactory.map(nowShowingSuccess, moviesFailure, genresSuccess)
        val expected = AsyncResult.failure<DiscoverViewState>(networkError())
        Assertions.assertThat(result).isEqualTo(expected)
    }

    @Test
    fun handlesGenresNetworkErrors() {
        val genresFailure = AsyncResult.failure<List<Genre>>(IOException())

        val result = DiscoverViewStateFactory.map(nowShowingSuccess, popularSuccess, genresFailure)

        val expected = AsyncResult.failure<DiscoverViewState>(networkError())
        Assertions.assertThat(result).isEqualTo(expected)
    }
}