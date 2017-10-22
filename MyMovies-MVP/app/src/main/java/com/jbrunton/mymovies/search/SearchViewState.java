package com.jbrunton.mymovies.search;

import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.ErrorViewState;
import com.jbrunton.mymovies.Movie;

import java.util.List;

@AutoValue
public abstract class SearchViewState extends ErrorViewState {
    public abstract List<SearchItemViewState> movies();

    public static Builder builder() {
        Builder builder = new AutoValue_SearchViewState.Builder();
        builder.setDefaults();
        return builder;
    }

    @AutoValue.Builder
    public abstract static class Builder extends ErrorViewState.Builder<Builder> {
        public abstract Builder setMovies(List<SearchItemViewState> movies);
        public abstract SearchViewState build();
    }
}