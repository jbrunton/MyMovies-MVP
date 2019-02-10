package com.jbrunton.mymovies.ui.search

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.repositories.ApplicationPreferences
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RepositoryFixtures
import com.jbrunton.mymovies.fixtures.TestSchedulerRule
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.networkError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class SearchViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulerRule = TestSchedulerRule()

    private lateinit var repository: MoviesRepository
    private lateinit var useCase: SearchUseCase
    private lateinit var preferences: ApplicationPreferences
    private val movieFactory = MovieFactory()
    private val MOVIE = movieFactory.create()
    private val NETWORK_ERROR = SocketTimeoutException()

    private val SUCCESS_VIEW_STATE = LoadingViewState.success(SearchViewState.from(listOf(MOVIE)))
    private val NETWORK_FAILURE_VIEW_STATE = LoadingViewState.failure(networkError(), MovieViewState.Empty)
    private val LOADING_VIEW_STATE = AsyncResult.Loading<MovieViewState>()

    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        repository = Mockito.mock(MoviesRepository::class.java)
        useCase = SearchUseCase(repository)
        preferences = Mockito.mock(ApplicationPreferences::class.java)
        viewModel = SearchViewModel(useCase)
        RepositoryFixtures.stubSearch(repository, "Star").toReturnDelayed(listOf(MOVIE), 1)

        viewModel.start()
        schedulerRule.TEST_SCHEDULER.triggerActions()
    }

    @Test
    fun startsWithEmptyState() {
        assertThat(viewModel.viewState.value).isEqualTo(SearchViewStateFactory.EmptyState)
    }

    @Test
    fun showsEmptyStateForEmptyQuery() {
        viewModel.performSearch("")
        schedulerRule.TEST_SCHEDULER.triggerActions()
        assertThat(viewModel.viewState.value).isEqualTo(SearchViewStateFactory.EmptyState)
    }

    @Test
    fun searchesForQuery() {
        viewModel.performSearch("Star")
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(2, TimeUnit.SECONDS)
        assertThat(viewModel.viewState.value).isEqualTo(SUCCESS_VIEW_STATE)
    }
}