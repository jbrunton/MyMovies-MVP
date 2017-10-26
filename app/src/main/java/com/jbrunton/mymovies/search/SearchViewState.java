package com.jbrunton.mymovies.search;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;

import java.util.List;

@AutoValue
public abstract class SearchViewState {
    public abstract LoadingViewState loadingViewState();
    public abstract List<MovieViewState> movies();

    public static Builder builder() {
        return new AutoValue_SearchViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMovies(List<MovieViewState> movies);
        public abstract Builder setLoadingViewState(LoadingViewState loadingViewState);
        public abstract SearchViewState build();
    }
}