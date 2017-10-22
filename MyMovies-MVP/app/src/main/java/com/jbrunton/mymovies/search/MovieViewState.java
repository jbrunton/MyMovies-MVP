package com.jbrunton.mymovies.search;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class MovieViewState {
    public abstract String movieId();
    public abstract String title();
    public abstract String yearReleased();
    public abstract String posterUrl();
    public abstract String rating();

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
        public abstract MovieViewState build();
    }
}
