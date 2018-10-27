package com.jbrunton.mymovies.moviedetails;

import com.google.common.base.Optional;
import com.jbrunton.entities.Movie;
import com.jbrunton.fixtures.MovieFactory;
import com.jbrunton.networking.DescriptiveError;
import com.jbrunton.mymovies.movies.MovieViewState;

import org.junit.Before;
import org.junit.Test;

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

        assertThat(viewState.getLoadingViewState()).isEqualTo(LegacyLoadingViewState.LOADING_STATE);
        assertThat(viewState.getMovie()).isEqualTo(MovieViewState.Companion.getEMPTY());
    }

    @Test public void createsErrorState() {
        MovieDetailsViewState viewState = factory.fromError(NETWORK_ERROR);

        assertThat(viewState.getLoadingViewState()).isEqualTo(loadingViewStateFactory.fromError(NETWORK_ERROR));
        assertThat(viewState.getMovie()).isEqualTo(MovieViewState.Companion.getEMPTY());
    }

    @Test public void createsContentState() {
        MovieDetailsViewState viewState = factory.fromMovie(MOVIE);

        assertThat(viewState.getMovie().getOverview()).isEqualTo(MOVIE.getOverview().get());
        // etc.
    }

    @Test public void setsBlankReleaseDateIfMissing() {
        MovieDetailsViewState viewState = factory.fromMovie(movieFactory.builder()
                .releaseDate(Optional.absent())
                .build());

        assertThat(viewState.getMovie().getYearReleased()).isEmpty();
    }
}