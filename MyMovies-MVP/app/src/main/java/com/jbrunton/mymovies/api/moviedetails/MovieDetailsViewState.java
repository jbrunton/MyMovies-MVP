package com.jbrunton.mymovies.api.moviedetails;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.Movie;
import com.jbrunton.mymovies.search.MovieViewState;
import com.jbrunton.mymovies.search.SearchViewState;

import java.util.Collections;
import java.util.Optional;

@AutoValue
public abstract class MovieDetailsViewState extends LoadingViewState {
    public abstract Optional<MovieViewState> movie();

    public static MovieDetailsViewState.Builder builder() {
        MovieDetailsViewState.Builder builder = new AutoValue_MovieDetailsViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    public static MovieDetailsViewState.Builder errorBuilder() {
        return builder()
                .setMovie(Optional.empty())
                .setCurrentState(LoadingViewState.State.ERROR);
    }

    @AutoValue.Builder
    public abstract static class Builder extends LoadingViewState.Builder<Builder> {
        public abstract Builder setMovie(Optional<MovieViewState> movie);
        public abstract MovieDetailsViewState build();
    }
}
