package com.jbrunton.mymovies.features.discover

import com.jbrunton.async.AsyncResult
import com.jbrunton.async.AsyncResult.Companion.success
import com.jbrunton.async.get
import com.jbrunton.mymovies.entities.models.Genre
import com.jbrunton.mymovies.features.discover.DiscoverStateChange.DiscoverResultsAvailable
import com.jbrunton.mymovies.fixtures.MovieFactory
import com.jbrunton.mymovies.libs.ui.livedata.SingleLiveEvent
import com.jbrunton.mymovies.usecases.discover.DiscoverState
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class DiscoverInteractorTest {

    @RelaxedMockK private lateinit var callbacks: DiscoverInteractor.Callbacks

    private lateinit var reducer: DiscoverInteractor

    private val movieFactory = MovieFactory()

    private val nowPlaying = listOf(movieFactory.create())
    private val popular = listOf(movieFactory.create())
    private val sciFi = Genre("1", "Sci-Fi")
    private val genres = listOf(sciFi)

    private val sciFiMovies = listOf(movieFactory.create())
    private val sciFiResult = success(listOf(movieFactory.create()))

    val discoverState = DiscoverState(
            nowPlaying,
            popular,
            genres
    )

    val successfulResult = success(discoverState)

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        reducer = DiscoverInteractor(mockk(), mockk(), callbacks)
    }

    @Test
    fun itUpdatesResults() {
        val previousState = AsyncResult.loading<DiscoverState>(null)
        val change = DiscoverResultsAvailable(successfulResult)

        val newState = reducer.combine(previousState, change)

        assertThat(newState).isEqualTo(successfulResult)
    }

    @Test
    fun itSelectsGenres() {
        val previousState = successfulResult
        val change = DiscoverStateChange.GenreSelected(sciFi)

        val newState = reducer.combine(previousState, change)

        assertThat(previousState.value.selectedGenre).isNull()
        assertThat(newState.get().selectedGenre).isEqualTo(sciFi)
    }

    @Test
    fun itUpdatesGenreResults() {
        val previousState = successfulResult
        val change = DiscoverStateChange.GenreResultsAvailable(sciFiResult)

        val newState = reducer.combine(previousState, change)

        assertThat(previousState.value.genreResults).isEmpty()
        assertThat(newState.get().genreResults).isEqualTo(sciFiResult.value)
        verify { callbacks.scrollToGenreResults() }
    }

    @Test
    fun itClearsSelectedGenreResults() {
        val previousState = success(discoverState.copy(genreResults = sciFiMovies, selectedGenre = sciFi))
        val change = DiscoverStateChange.SelectedGenreCleared

        val newState = reducer.combine(previousState, change)

        assertThat(previousState.value.genreResults).isNotEmpty
        assertThat(newState.get().genreResults).isEmpty()
        assertThat(newState.get().selectedGenre).isNull()
    }
}