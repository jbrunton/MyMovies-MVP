package com.jbrunton.mymovies.search;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;

import java.util.Collections;
import java.util.List;

@AutoValue
public abstract class SearchViewState extends LoadingViewState {
    public abstract List<MovieViewState> movies();

    public static Builder builder() {
        Builder builder = new AutoValue_SearchViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    public static Builder errorBuilder() {
        return builder()
                .setMovies(Collections.emptyList())
                .setCurrentState(LoadingViewState.State.ERROR);
    }

    @AutoValue.Builder
    public abstract static class Builder extends LoadingViewState.Builder<Builder> {
        public abstract Builder setMovies(List<MovieViewState> movies);
        public abstract SearchViewState build();
    }
}