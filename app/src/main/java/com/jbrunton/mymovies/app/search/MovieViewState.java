package com.jbrunton.mymovies.app.search;

import com.google.auto.value.AutoValue;

import java.util.Optional;

@AutoValue
public abstract class MovieViewState {
    public abstract String movieId();
    public abstract String title();
    public abstract String yearReleased();
    public abstract String posterUrl();
    public abstract String rating();
    public abstract Optional<String> overview();

    public static Builder builder() {
        return new AutoValue_MovieViewState.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder setMovieId(String movieId);
        public abstract Builder setTitle(String title);
        public abstract Builder setYearReleased(String yearReleased);
        public abstract Builder setPosterUrl(String posterUrl);
        public abstract Builder setRating(String rating);
        public abstract Builder setOverview(Optional<String> overview);
        public abstract MovieViewState build();
    }
}
