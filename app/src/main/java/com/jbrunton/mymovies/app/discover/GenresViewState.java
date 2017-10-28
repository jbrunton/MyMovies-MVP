package com.jbrunton.mymovies.app.discover;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Genre;

import java.util.List;

@AutoValue
public abstract class GenresViewState {
    public abstract LoadingViewState loadingViewState();
    public abstract List<Genre> genres();

    public static Builder builder() {
        return new AutoValue_GenresViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setGenres(List<Genre> genres);
        public abstract Builder setLoadingViewState(LoadingViewState loadingViewState);
        public abstract GenresViewState build();
    }
}