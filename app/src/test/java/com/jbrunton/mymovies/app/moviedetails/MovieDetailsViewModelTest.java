package com.jbrunton.mymovies.app.moviedetails;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jbrunton.entities.Movie;
import com.jbrunton.fixtures.MovieFactory;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.api.repositories.HttpMoviesRepository;
import com.jbrunton.mymovies.fixtures.TestSchedulerRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static com.jbrunton.mymovies.fixtures.RepositoryFixtures.stubFind;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class MovieDetailsViewModelTest {

    @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Rule public TestSchedulerRule schedulerRule = new TestSchedulerRule();

    private HttpMoviesRepository repository;
    private final MovieFactory movieFactory = new MovieFactory();
    private final Movie MOVIE = movieFactory.create();
    private final DescriptiveError NETWORK_ERROR = new DescriptiveError("Network Error", true);
    private final MovieDetailsViewStateFactory viewStateFactory = new MovieDetailsViewStateFactory();

    private MovieDetailsViewModel viewModel;

    @Before public void setUp() {
        repository = mock(HttpMoviesRepository.class);
        viewModel = new MovieDetailsViewModel("1", repository);
        stubFind(repository, "1").toReturnDelayed(MOVIE, 1);
    }

    @Test public void startsWithLoadingState() {
        viewModel.start();
        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.loadingState());
    }

    @Test public void loadsMovie() {
        viewModel.start();
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS);
        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.fromMovie(MOVIE));
    }

    @Test public void displaysFailureOnError() {
        stubFind(repository, "1").toErrorWithDelayed(NETWORK_ERROR, 1);
        viewModel.start();

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS);

        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.fromError(NETWORK_ERROR));
    }

    @Test public void showsLoadingStateWhenRetrying() {
        stubFind(repository, "1").toErrorWithDelayed(NETWORK_ERROR, 1);
        viewModel.start();
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS);
        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.fromError(NETWORK_ERROR));

        stubFind(repository, "1").toReturnDelayed(MOVIE, 1);
        viewModel.retry();

        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.loadingState());
    }

    @Test public void loadsMovieAfterRetrying() {
        stubFind(repository, "1").toErrorWith(NETWORK_ERROR);
        viewModel.start();

        stubFind(repository, "1").toReturnDelayed(MOVIE, 1);
        viewModel.retry();

        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS);
        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.fromMovie(MOVIE));
    }
}