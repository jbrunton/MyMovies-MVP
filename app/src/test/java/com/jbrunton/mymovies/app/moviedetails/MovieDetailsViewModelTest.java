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

    @Before public void setUp() {
        repository = mock(MoviesRepository.class);
        when(repository.getMovie("1")).thenReturn(Observable.just(MOVIE).delay(1, TimeUnit.SECONDS));
        viewModel = new MovieDetailsViewModel("1", repository);
    }

    @Test public void startsWithLoadingState() {
        assertThat(viewModel.viewState().getValue()).isEqualTo(MovieDetailsViewState.buildLoadingState());
    }

}