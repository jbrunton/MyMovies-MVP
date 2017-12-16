package com.jbrunton.mymovies.movies;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MovieViewState extends BaseMovieViewState {
    public abstract String overview();

    public static Builder builder() {
        return new AutoValue_MovieViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder extends BaseMovieViewState.Builder<Builder> {
        public abstract Builder overview(String overview);
        public abstract MovieViewState build();
    }

    public static final MovieViewState EMPTY = builder()
            .movieId("")
            .title("")
            .yearReleased("")
            .posterUrl("")
            .backdropUrl("")
            .rating("")
            .overview("")
            .build();
}
