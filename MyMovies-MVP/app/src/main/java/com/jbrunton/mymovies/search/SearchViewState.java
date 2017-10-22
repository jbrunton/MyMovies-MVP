package com.jbrunton.mymovies.search;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;

import java.util.List;

@AutoValue
public abstract class SearchViewState extends LoadingViewState {
    public abstract List<SearchItemViewState> movies();

    public static Builder builder() {
        Builder builder = new AutoValue_SearchViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    @AutoValue.Builder
    public abstract static class Builder extends LoadingViewState.Builder<Builder> {
        public abstract Builder setMovies(List<SearchItemViewState> movies);
        public abstract SearchViewState build();
    }
}