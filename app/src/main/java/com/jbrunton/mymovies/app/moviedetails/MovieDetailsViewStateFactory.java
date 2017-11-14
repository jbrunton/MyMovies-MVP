package com.jbrunton.mymovies.app.moviedetails;

import com.jbrunton.mymovies.app.movies.BaseMovieViewStateFactory;
import com.jbrunton.mymovies.app.movies.MovieViewState;
import com.jbrunton.mymovies.app.shared.LoadingViewState;
import com.jbrunton.mymovies.models.Movie;

public class MovieDetailsViewStateFactory extends BaseMovieViewStateFactory {
    public MovieDetailsViewState fromMovie(Movie movie) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.OK_STATE)
                .setMovie(toMovieViewState(movie))
                .build();
    }

    public MovieDetailsViewState fromError(Throwable throwable) {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(loadingViewStateFactory.fromError(throwable))
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    public MovieDetailsViewState loadingState() {
        return MovieDetailsViewState.builder()
                .setLoadingViewState(LoadingViewState.LOADING_STATE)
                .setMovie(MovieViewState.EMPTY)
                .build();
    }

    private MovieViewState toMovieViewState(Movie movie) {
        return setDefaults(MovieViewState.builder(), movie)
                .overview(movie.overview().get())
                .build();
    }
}
