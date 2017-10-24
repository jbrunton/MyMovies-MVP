package com.jbrunton.mymovies.discover;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.LoadingViewState;
import com.jbrunton.mymovies.models.Genre;

import java.util.List;

@AutoValue
public abstract class GenresViewState extends LoadingViewState {
    public abstract List<Genre> genres();

    public static Builder builder() {
        Builder builder = new AutoValue_GenresViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    @AutoValue.Builder
    public abstract static class Builder extends LoadingViewState.Builder<Builder> {
        public abstract Builder setGenres(List<Genre> genres);
        public abstract GenresViewState build();
    }
}