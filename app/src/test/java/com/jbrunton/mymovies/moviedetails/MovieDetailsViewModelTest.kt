package com.jbrunton.mymovies.moviedetails

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.jbrunton.entities.MoviesRepository
import com.jbrunton.fixtures.MovieFactory
import com.jbrunton.mymovies.fixtures.RepositoryFixtures.stubFind
import com.jbrunton.mymovies.fixtures.TestSchedulerRule
import com.jbrunton.networking.DescriptiveError
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import java.util.concurrent.TimeUnit

class MovieDetailsViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    var schedulerRule = TestSchedulerRule()

    private var repository: MoviesRepository? = null
    private val movieFactory = MovieFactory()
    private val MOVIE = movieFactory.create()
    private val NETWORK_ERROR = DescriptiveError("Network Error", true)
    private val viewStateFactory = MovieDetailsViewStateFactory()

    private lateinit var viewModel: MovieDetailsViewModel

    @Before
    fun setUp() {
        repository = mock(MoviesRepository::class.java)
        viewModel = MovieDetailsViewModel("1", repository!!)
        stubFind(repository, "1").toReturnDelayed(MOVIE, 1)
    }

    @Test
    fun startsWithLoadingState() {
        viewModel.start()
        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.loadingState())
    }

    @Test
    fun loadsMovie() {
        viewModel.start()
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.fromMovie(MOVIE))
    }

    @Test
    fun displaysFailureOnError() {
        stubFind(repository, "1").toErrorWithDelayed(NETWORK_ERROR, 1)
        viewModel.start()

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)

        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.fromError(NETWORK_ERROR))
    }

    @Test
    fun showsLoadingStateWhenRetrying() {
        stubFind(repository, "1").toErrorWithDelayed(NETWORK_ERROR, 1)
        viewModel.start()
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.fromError(NETWORK_ERROR))

        stubFind(repository, "1").toReturnDelayed(MOVIE, 1)
        viewModel.retry()

        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.loadingState())
    }

    @Test
    fun loadsMovieAfterRetrying() {
        stubFind(repository, "1").toErrorWith(NETWORK_ERROR)
        viewModel.start()

        stubFind(repository, "1").toReturnDelayed(MOVIE, 1)
        viewModel.retry()

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS)
        assertThat(viewModel.viewState().value).isEqualTo(viewStateFactory.fromMovie(MOVIE))
    }
}