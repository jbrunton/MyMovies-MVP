package com.jbrunton.mymovies.app.movies;

public abstract class BaseMovieViewState {
    public abstract String movieId();
    public abstract String title();
    public abstract String yearReleased();
    public abstract String posterUrl();
    public abstract String backdropUrl();
    public abstract String rating();

    public abstract static class Builder<T extends Builder> {
        public abstract T movieId(String movieId);
        public abstract T title(String title);
        public abstract T yearReleased(String yearReleased);
        public abstract T posterUrl(String posterUrl);
        public abstract T backdropUrl(String backdropUrl);
        public abstract T rating(String rating);
    }
}
