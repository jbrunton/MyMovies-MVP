package com.jbrunton.mymovies.ui.moviedetails

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.async.AsyncResult
import com.jbrunton.entities.models.Movie
import com.jbrunton.entities.repositories.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RepositoryFixtures.stubFind
import com.jbrunton.mymovies.fixtures.TestSchedulerRule
import com.jbrunton.mymovies.ui.movies.MovieViewState
import com.jbrunton.mymovies.ui.shared.LoadingViewState
import com.jbrunton.mymovies.ui.shared.networkError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.net.SocketTimeoutException
import java.util.concurrent.TimeUnit

class MovieDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var schedulerRule = TestSchedulerRule()

    private lateinit var repository: MoviesRepository
    private val movieFactory = MovieFactory()
    private val MOVIE = movieFactory.create()
    private val NETWORK_ERROR = SocketTimeoutException()

    private val SUCCESS_VIEW_STATE = LoadingViewState.success(MovieViewState(MOVIE))
    private val NETWORK_FAILURE_VIEW_STATE = LoadingViewState.failure(networkError(), MovieViewState(Movie.emptyMovie))
    private val LOADING_VIEW_STATE = AsyncResult.Loading<MovieViewState>()

    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        repository = mock(MoviesRepository::class.java)
        viewModel = MovieDetailsViewModel("1", repository)
        stubFind(repository, "1").toReturnDelayed(MOVIE, 1)
    }

    @Test
    fun loadsMovie() {
        viewModel.start()
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        assertThat(viewModel.viewState.value).isEqualTo(SUCCESS_VIEW_STATE)
    }

    @Test
    fun displaysFailureOnError() {
        stubFind(repository, "1").toErrorWithDelayed(NETWORK_ERROR, 1)
        viewModel.start()

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)

        assertThat(viewModel.viewState.value).isEqualTo(NETWORK_FAILURE_VIEW_STATE)
    }

    @Test
    fun loadsMovieAfterRetrying() {
        stubFind(repository, "1").toErrorWith(NETWORK_ERROR)
        viewModel.start()

        stubFind(repository, "1").toReturnDelayed(MOVIE, 1)
        viewModel.retry()

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        assertThat(viewModel.viewState.value).isEqualTo(SUCCESS_VIEW_STATE)
    }
}