package com.jbrunton.mymovies.movies;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MovieSearchResultViewState extends BaseMovieViewState {
    public static Builder builder() {
        return new AutoValue_MovieSearchResultViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder extends BaseMovieViewState.Builder<Builder> {
        public abstract MovieSearchResultViewState build();
    }
}
