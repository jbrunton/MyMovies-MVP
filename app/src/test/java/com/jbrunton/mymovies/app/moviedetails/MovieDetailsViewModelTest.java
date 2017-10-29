package com.jbrunton.mymovies.app.moviedetails;

import android.arch.core.executor.testing.InstantTaskExecutorRule;

import com.jbrunton.mymovies.MovieFactory;
import com.jbrunton.mymovies.TestSchedulerRule;
import com.jbrunton.mymovies.api.repositories.MoviesRepository;
import com.jbrunton.mymovies.models.Movie;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MovieDetailsViewModelTest {

    @Rule public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();
    @Rule public TestSchedulerRule schedulerRule = new TestSchedulerRule();

    private MovieDetailsViewModel viewModel;
    private MoviesRepository repository;
    private final MovieFactory movieFactory = new MovieFactory();
    private final Movie MOVIE = movieFactory.create();
    private final MovieDetailsViewStateFactory viewStateFactory = new MovieDetailsViewStateFactory();

    @Before public void setUp() {
        repository = mock(MoviesRepository.class);
        stubFind(repository, "1").toReturnDelayed(MOVIE, 1);
        viewModel = new MovieDetailsViewModel("1", repository);
    }

    @Test public void startsWithLoadingState() {
        assertThat(viewModel.viewState().getValue()).isEqualTo(MovieDetailsViewState.buildLoadingState());
    }

    @Test public void loadsMovie() {
        schedulerRule.TEST_SCHEDULER.advanceTimeBy(1, TimeUnit.SECONDS);
        assertThat(viewModel.viewState().getValue()).isEqualTo(viewStateFactory.fromMovie(MOVIE));
    }

    private static class FakeMoviesRepositoryDsl {
        protected final MoviesRepository repository;

        private FakeMoviesRepositoryDsl(MoviesRepository repository) {
            this.repository = repository;
        }
    }

    private static class FakeMoviesFindDsl extends FakeMoviesRepositoryDsl {
        private final String id;

        private FakeMoviesFindDsl(MoviesRepository repository, String id) {
            super(repository);
            this.id = id;
        }

        public void toReturn(Movie movie) {
            toReturnDelayed(movie, 0);
        }

        public void toReturnDelayed(Movie movie, int delay) {
            when(repository.getMovie(id)).thenReturn(Observable.just(movie).delay(delay, TimeUnit.SECONDS));
        }

        public void toErrorWith(Throwable throwable) {
            toErrorWithDelayed(throwable, 0);
        }

        public void toErrorWithDelayed(Throwable throwable, int delay) {
            when(repository.getMovie(id)).thenReturn(Observable.<Movie>error(throwable).delay(delay, TimeUnit.SECONDS));
        }
    }

    private static FakeMoviesFindDsl stubFind(MoviesRepository repository, String id) {
        return new FakeMoviesFindDsl(repository, id);
    }
}