package com.jbrunton.mymovies.search;

import android.support.annotation.DrawableRes;

import com.google.auto.value.AutoValue;
import com.jbrunton.mymovies.Movie;

import java.util.List;

@AutoValue
public abstract class SearchItemViewState {
    public abstract String title();
    public abstract String yearReleased();
    public abstract String posterUrl();
    public abstract String rating();

    static Builder builder() {
        return new AutoValue_SearchItemViewState.Builder();
    }

    @AutoValue.Builder
    abstract static class Builder {
        public abstract Builder setTitle(String title);
        public abstract Builder setYearReleased(String yearReleased);
        public abstract Builder setPosterUrl(String posterUrl);
        public abstract Builder setRating(String rating);
        abstract SearchItemViewState build();
    }
}
