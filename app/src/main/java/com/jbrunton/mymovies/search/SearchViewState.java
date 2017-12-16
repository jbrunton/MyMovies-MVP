package com.jbrunton.mymovies.search;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.movies.MovieSearchResultViewState;
import com.jbrunton.mymovies.shared.LoadingViewState;

import java.util.List;

@AutoValue
public abstract class SearchViewState {
    public abstract LoadingViewState loadingViewState();
    public abstract List<MovieSearchResultViewState> movies();

    public static Builder builder() {
        return new AutoValue_SearchViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMovies(List<MovieSearchResultViewState> movies);
        public abstract Builder setLoadingViewState(LoadingViewState loadingViewState);
        public abstract SearchViewState build();
    }
}