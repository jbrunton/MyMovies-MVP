package com.jbrunton.mymovies.app.moviedetails;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;

@AutoValue
public abstract class MovieDetailsViewState {
    public abstract LoadingViewState loadingViewState();
    public abstract MovieViewState movie();

    public static MovieDetailsViewState.Builder builder() {
        return new AutoValue_MovieDetailsViewState.Builder();
    }

    public static MovieDetailsViewState buildLoadingState() {
        return builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setLoadingViewState(LoadingViewState loadingViewState);
        public abstract Builder setMovie(MovieViewState movie);
        public abstract MovieDetailsViewState build();
    }
}
