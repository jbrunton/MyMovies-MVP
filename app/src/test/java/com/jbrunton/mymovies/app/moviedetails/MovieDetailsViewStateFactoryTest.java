package com.jbrunton.mymovies.app.moviedetails;

import com.jbrunton.entities.Movie;
import com.jbrunton.fixtures.MovieFactory;
import com.jbrunton.mymovies.api.DescriptiveError;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewStateFactory;

import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class MovieDetailsViewStateFactoryTest {
    private static final Throwable NETWORK_ERROR = new DescriptiveError("Network Error", true);
    private final MovieFactory movieFactory = new MovieFactory();
    private final Movie MOVIE = movieFactory.create();
    private final LoadingViewStateFactory loadingViewStateFactory = new LoadingViewStateFactory();

    private MovieDetailsViewStateFactory factory;

    @Before public void setUp() {
        factory = new MovieDetailsViewStateFactory();
    }

    @Test public void createsLoadingState() {
        MovieDetailsViewState viewState = factory.loadingState();

        assertThat(viewState.loadingViewState()).isEqualTo(LoadingViewState.LOADING_STATE);
        assertThat(viewState.movie()).isEqualTo(MovieViewState.EMPTY);
    }

    @Test public void createsErrorState() {
        MovieDetailsViewState viewState = factory.fromError(NETWORK_ERROR);

        assertThat(viewState.loadingViewState()).isEqualTo(loadingViewStateFactory.fromError(NETWORK_ERROR));
        assertThat(viewState.movie()).isEqualTo(MovieViewState.EMPTY);
    }

    @Test public void createsContentState() {
        MovieDetailsViewState viewState = factory.fromMovie(MOVIE);

        assertThat(viewState.movie().overview()).isEqualTo(MOVIE.overview().get());
        // etc.
    }

    @Test public void setsBlankReleaseDateIfMissing() {
        MovieDetailsViewState viewState = factory.fromMovie(movieFactory.builder()
                .releaseDate(Optional.empty())
                .build());

        assertThat(viewState.movie().yearReleased()).isEmpty();
    }
}