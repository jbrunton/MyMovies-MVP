package com.jbrunton.mymovies.discover;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.ErrorViewState;
import com.jbrunton.mymovies.models.Genre;
import com.jbrunton.mymovies.search.SearchItemViewState;

import java.util.List;

@AutoValue
public abstract class GenresViewState extends ErrorViewState {
    public abstract List<Genre> genres();

    public static Builder builder() {
        Builder builder = new AutoValue_GenresViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    @AutoValue.Builder
    public abstract static class Builder extends ErrorViewState.Builder<Builder> {
        public abstract Builder setGenres(List<Genre> genres);
        public abstract GenresViewState build();
    }
}