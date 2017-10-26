package com.jbrunton.mymovies.moviedetails;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.search.MovieViewState;

import java.util.Optional;

@AutoValue
public abstract class MovieDetailsViewState {
    public abstract LoadingViewState loadingViewState();
    public abstract Optional<MovieViewState> movie();

    public static MovieDetailsViewState.Builder builder() {
        return new AutoValue_MovieDetailsViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLoadingViewState(LoadingViewState loadingViewState);
        public abstract Builder setMovie(Optional<MovieViewState> movie);
        public abstract MovieDetailsViewState build();
    }
}
